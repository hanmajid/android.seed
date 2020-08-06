package com.hanmajid.android.seed.ui.connectivity.wifi.scan

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.hanmajid.android.seed.databinding.FragmentWifiScanBinding
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiStateBroadcastListener
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiStateConnectivityNetworkCallback
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiUtil
import com.hanmajid.android.seed.util.PermissionUtil

class WifiScanFragment : Fragment() {

    private lateinit var binding: FragmentWifiScanBinding

    private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val locationManager: LocationManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val adapter = WifiScanListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWifiScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        // Listen to Wi-Fi scan results.
        WifiScanBroadcastReceiver(
            viewLifecycleOwner,
            requireContext(),
            wifiManager,
            true,
            { scanResults ->
                updateList(scanResults, true)
            }, { scanResults ->
                updateList(scanResults, false)
            }
        )

        // Listen to connected Wi-Fi changes to get connected Wi-Fi information.
        val networkResult = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(
            networkResult,
            WifiStateConnectivityNetworkCallback(requireContext())
        )
        WifiStateBroadcastListener(
            viewLifecycleOwner,
            requireContext(),
            { _, _ ->
                // Do nothing
            }, {
                refreshConnectedWifiInfo()
            }
        )

        refreshUI()
    }

    private fun refreshUI() {
        refreshScan()
        refreshConnectedWifiInfo()
    }

    private fun refreshConnectedWifiInfo() {
        val wifiInfo = WifiUtil.getCurrentlyConnectedWifiInfo(wifiManager)
        adapter.connectedWifiInfo = wifiInfo.takeIf { it.ssid != "<unknown ssid>" }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.isRefreshing = true

        val intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                refreshUI()
            }
        binding.buttonLocationSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
        }

        binding.buttonAppSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                }
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            refreshUI()
        }
    }

    private fun refreshScan() {
        binding.isPermissionDenied = false
        if (PermissionUtil.allPermissionsGranted(
                requireContext(),
                REQUIRED_PERMISSIONS
            )
        ) {
            doWifiScan()
        } else {
            PermissionUtil.requestAllPermissions(
                this,
                binding.root,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun doWifiScan() {
        val isLocationEnabled = LocationManagerCompat.isLocationEnabled(locationManager)
        binding.isLocationDisabled = !isLocationEnabled
        if (isLocationEnabled) {
            WifiUtil.startScanning(
                wifiManager,
                true
            ) { scanResults ->
                updateList(scanResults, false)
            }
        } else {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun updateList(scanResults: List<ScanResult>, success: Boolean) {
        if (!success) {
            Snackbar.make(
                binding.root,
                "Try disabling Wi-Fi scanning throttling in developer options to scan Wi-Fi frequently.",
                Snackbar.LENGTH_SHORT
            )
                .setAction("OK") {

                }
                .show()
        }
        adapter.submitList(scanResults)
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (PermissionUtil.allPermissionsGranted(
                    requireContext(),
                    REQUIRED_PERMISSIONS
                )
            ) {
                doWifiScan()
            } else {
                binding.swipeRefresh.isRefreshing = false
                binding.isPermissionDenied = true
            }
        }
    }

    companion object {
        private const val TAG = "WifiScanFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}