package com.hanmajid.android.seed.ui.connectivity.wifi

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hanmajid.android.seed.databinding.FragmentWifiBinding

class WifiFragment : Fragment() {

    private lateinit var binding: FragmentWifiBinding

    private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
            MyConnectivityManager(requireContext())
        )

        // Listen to Wi-Fi state changes here.
        WifiStateBroadcastListener(
            viewLifecycleOwner,
            requireContext(),
            { prevState, state ->
                updateWifiStatusText(state)
            }, {
                updateConnectedWifiText()
            }
        )
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        updateWifiStatusText(wifiManager.wifiState)
        updateConnectedWifiText()

        binding.wifiP2pStatus.text =
            if (wifiManager.isP2pSupported) "P2P: Supported" else "P2P: Not supported"

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

        binding.buttonWifiSettings.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_WIFI_SETTINGS
                )
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            updateWifiStatusText(wifiManager.wifiState)
            updateConnectedWifiText()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun updateWifiStatusText(state: Int) {
        val status = WifiUtil.getWifiStateDescription(state)
        binding.wifiStatus.text = "Wi-Fi Status: $status"
    }

    private fun updateConnectedWifiText() {
        val wifiInfo = WifiUtil.getCurrentlyConnectedWifiInfo(wifiManager)
        val ssid = if (wifiInfo.ssid != "<unknown ssid>") wifiInfo.ssid else "-"
        binding.connectedWifi.text =
            "Connected Wi-Fi: $ssid"
    }

    class MyConnectivityManager(
        val context: Context
    ) :
        ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            sendBroadcast()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            sendBroadcast()
        }

        private fun sendBroadcast() {
            context.sendBroadcast(
                Intent().apply {
                    action = WifiStateBroadcastListener.WIFI_STATE_CONNECTION_CHANGES_ACTION
                }
            )
        }
    }

    companion object {
        private const val TAG = "WifiFragment"
    }
}