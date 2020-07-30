package com.hanmajid.android.seed.ui.connectivity.wifi

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import com.hanmajid.android.seed.databinding.FragmentWifiBinding
import com.hanmajid.android.seed.util.PermissionUtil

class WifiFragment : Fragment() {

    private lateinit var binding: FragmentWifiBinding
    private lateinit var wifiManager: WifiManager
    private lateinit var locationManager: LocationManager

    private val adapter = WifiListAdapter()

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

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Step 1. Get WifiManager from systemService.
        wifiManager = requireContext().getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Step 2. Instantiate [WifiScanBroadcastReceiver]
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

        refreshScan()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.isRefreshing = true

        binding.buttonLocationSettings.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
        }

        binding.buttonAppSettings.setOnClickListener {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                }
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            refreshScan()
        }
    }

    private fun refreshScan() {
        binding.isPermissionDenied = false
        if (PermissionUtil.allPermissionsGranted(requireContext(), REQUIRED_PERMISSIONS)) {
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

    // Called when first instantiating
    private fun doWifiScan() {
        val isLocationEnabled = LocationManagerCompat.isLocationEnabled(locationManager)
        binding.isLocationDisabled = !isLocationEnabled
        if (isLocationEnabled) {
            WifiUtil.startScanning(wifiManager, true) { scanResults ->
                updateList(scanResults, false)
            }
        } else {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun updateList(scanResults: List<ScanResult>, success: Boolean) {
        if (!success) {
            Toast.makeText(
                context,
                "The results displayed may not be accurate as your phone enables Wi-Fi scanning throttling",
                Toast.LENGTH_SHORT
            ).show()
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
            if (PermissionUtil.allPermissionsGranted(requireContext(), REQUIRED_PERMISSIONS)) {
                doWifiScan()
            } else {
                binding.swipeRefresh.isRefreshing = false
                binding.isPermissionDenied = true
            }
        }
    }

    companion object {
        private const val TAG = "WifiFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}