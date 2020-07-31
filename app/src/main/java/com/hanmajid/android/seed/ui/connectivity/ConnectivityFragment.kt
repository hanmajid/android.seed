package com.hanmajid.android.seed.ui.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hanmajid.android.seed.databinding.FragmentConnectivityBinding

class ConnectivityFragment : Fragment() {

    private lateinit var binding: FragmentConnectivityBinding

    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConnectivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        //        val network: Network = connectivityManager.activeNetwork
        val networks = connectivityManager.allNetworks
        connectivityManager.isActiveNetworkMetered
        val networkRequest = NetworkRequest.Builder()
            .build()
        connectivityManager.registerNetworkCallback(
            networkRequest,
            MyConnectivityManager(connectivityManager)
        )
//        val capabilities = connectivityManager.getNetworkCapabilities(network)
//        Log.wtf("NETWORK", network?.toString())
        Log.wtf("NETWORKS", networks.toString())
//        Log.wtf("CAPAB", capabilities?.toString())
//        connectivityManager.registerDefaultNetworkCallback(MyConnectivityManager())
//        val x = MyConnectivityManager()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonWifi.setOnClickListener {
            findNavController().navigate(
                ConnectivityFragmentDirections.actionConnectivityFragmentToConnectivityWifiNavGraph()
            )
        }
    }

    class MyConnectivityManager(
        val connectivityManager: ConnectivityManager
    ) :
        ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.wtf(
                "CM",
                "Available $network (${connectivityManager.allNetworks.size}) ${connectivityManager.isDefaultNetworkActive}"
            )
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.wtf("CM", "Losing ${maxMsToLive}")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.wtf(
                "CM",
                "Lost $network (${connectivityManager.allNetworks.size}) ${connectivityManager.isDefaultNetworkActive}"
            )
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Log.wtf("CM", "LINK: $linkProperties")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.wtf("CM", "Unavailable")
        }
    }

    companion object {
        private const val TAG = "ConnectivityFragment"
    }
}