package com.hanmajid.android.seed.ui.connectivity.wifi.suggestion

import android.content.Context
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.hanmajid.android.seed.databinding.FragmentWifiSuggestionBinding
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiUtil
import com.hanmajid.android.seed.util.PermissionUtil

class WifiSuggestionFragment : Fragment() {

    private lateinit var binding: FragmentWifiSuggestionBinding
    private lateinit var adapter: WifiSuggestionListAdapter

    private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    private val locationManager: LocationManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWifiSuggestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        setupSuggestions()
        setupBroadcastListener()
    }

    private fun setupBroadcastListener() {
        WifiSuggestionConnectedBroadcastListener(
            viewLifecycleOwner,
            requireContext(),
            {
                // On success
                binding.isConnected = true
            },
            {
                // On error API level not sufficient.
                binding.connectionStatus = "API level must be 29 or above"
            }
        )
        refreshLocation()
    }

    private fun refreshLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PermissionUtil.requestPermissionsIfNeeded(
                this,
                REQUIRED_PERMISSIONS
            ) {
                var isGranted = true
                it?.apply {
                    isGranted = this.values.all { granted -> granted }
                }
                if (isGranted) {
                    // Check location
                    val isLocationEnabled = LocationManagerCompat.isLocationEnabled(locationManager)
                    if (!isLocationEnabled) {
                        // Location service is disabled
                        binding.connectionStatus = "Please enable location service first."
                    } else {
                        binding.connectionStatus =
                            "Ready! If a suggested network is connected you will be notified!"
                    }
                } else {
                    // Location permission is not given.
                    binding.connectionStatus = "Please allow location permission first."
                }
            }
        }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = WifiSuggestionListAdapter(
            { suggestion ->
                WifiSuggestionUtil.addWifiNetworkSuggestions(
                    this,
                    wifiManager,
                    listOf(suggestion),
                    {
                        val message =
                            if (it == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) "${suggestion.ssid} is suggested!" else
                                WifiUtil.getWifiSuggestionStatusDescription(it)
                        Snackbar.make(
                            binding.root,
                            message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    },
                    {
                        val errorMessage = when (it) {
                            WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_PERMISSIONS -> "Permission not granted"
                            WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_API_LEVEL -> "Wi-Fi suggestion need API level 29 or above :("
                            else -> ""
                        }
                        Snackbar.make(
                            binding.root,
                            errorMessage,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                )
            },
            { suggestion ->
                WifiSuggestionUtil.removeWifiNetworkSuggestions(
                    this,
                    wifiManager,
                    listOf(suggestion),
                    {
                        val message =
                            if (it == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) "${suggestion.ssid} is removed!" else
                                WifiUtil.getWifiSuggestionStatusDescription(it)
                        Snackbar.make(
                            binding.root,
                            message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    },
                    {
                        val errorMessage = when (it) {
                            WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_PERMISSIONS -> "Permission not granted"
                            WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_API_LEVEL -> "Wi-Fi suggestion need API level 29 or above :("
                            else -> ""
                        }
                        Snackbar.make(
                            binding.root,
                            errorMessage,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                )
            }
        )
        binding.recyclerView.adapter = adapter

        binding.buttonListener.setOnClickListener {
            refreshLocation()
        }
    }

    private fun setupSuggestions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            adapter.isWifiSuggestionEnabled = true

            // Note: Change this to actual Wi-Fi ssid & password to test.
            val suggestion1 = MyWifiSuggestion(
                ssid = "hanmajid-wifi-password",
                priority = 1,
                wpa2Passphrase = "password",
                isAppInteractionRequired = true
            )
            val suggestion2 = MyWifiSuggestion(
                ssid = "hanmajid-wifi-open",
                priority = 2,
                isAppInteractionRequired = false
            )
            val suggestions = listOf(suggestion1, suggestion2)
            adapter.submitList(suggestions)
        } else {
            Snackbar.make(
                binding.root,
                "Wi-Fi suggestion need API level 29 or above :(",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val TAG = "WifiSuggestionFragment"
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}