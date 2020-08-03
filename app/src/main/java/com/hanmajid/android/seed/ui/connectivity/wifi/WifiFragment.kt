package com.hanmajid.android.seed.ui.connectivity.wifi

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hanmajid.android.seed.databinding.FragmentWifiBinding
import com.hanmajid.android.seed.util.PermissionUtil

class WifiFragment : Fragment() {

    private lateinit var binding: FragmentWifiBinding

    private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val locationManager: LocationManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWifiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        // Listen to connected Wi-Fi changes here.
        val networkResult = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(
            networkResult,
            WifiStateConnectivityNetworkCallback(requireContext())
        )

        // Listen to Wi-Fi state changes here.
        WifiStateBroadcastListener(
            viewLifecycleOwner,
            requireContext(),
            { _, state ->
                updateWifiStatus(state)
            }, {
                updateConnectedWifiText()
            }
        )
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        refreshUI()

        binding.buttonWifiScan.setOnClickListener {
            findNavController().navigate(
                WifiFragmentDirections.actionWifiFragmentToWifiScanFragment()
            )
        }

        binding.buttonWifiP2p.setOnClickListener {
            findNavController().navigate(
                WifiFragmentDirections.actionWifiFragmentToWifiP2pFragment()
            )
        }

        val intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                refreshUI()
            }
        binding.buttonWifiSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(
                    Settings.ACTION_WIFI_SETTINGS
                )
            )
        }
        binding.buttonLocationSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
        }
        binding.buttonPermissionSettings.setOnClickListener {
            intentLauncher.launch(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                }
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            refreshUI()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun refreshUI() {
        updateWifiStatus(wifiManager.wifiState)
        updateConnectedWifiText()
    }

    private fun updateWifiStatus(state: Int = wifiManager.wifiState) {
        binding.isWifiEnabled =
            state == WifiManager.WIFI_STATE_ENABLING || state == WifiManager.WIFI_STATE_ENABLED
        binding.wifiState = WifiUtil.getWifiStateDescription(state)
    }

    private fun updateConnectedWifiText() {
        binding.isPermissionGranted = true
        if (PermissionUtil.allPermissionsGranted(
                requireContext(),
                REQUIRED_PERMISSIONS
            )
        ) {
            refreshConnectedWifiInfo()
        } else {
            PermissionUtil.requestAllPermissions(
                this,
                binding.root,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.isP2pSupported = wifiManager.isP2pSupported
    }

    private fun refreshConnectedWifiInfo() {
        val isLocationEnabled = LocationManagerCompat.isLocationEnabled(locationManager)
        binding.isLocationEnabled = isLocationEnabled

        val wifiInfo = WifiUtil.getCurrentlyConnectedWifiInfo(wifiManager)
        binding.connectedWifi = if (wifiInfo.ssid != "<unknown ssid>") wifiInfo.ssid else "-"
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
                refreshConnectedWifiInfo()
            } else {
                binding.isPermissionGranted = false
            }
        }
    }

    companion object {
        private const val TAG = "WifiFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}