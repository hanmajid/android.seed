package com.hanmajid.android.seed.ui.connectivity.wifi.p2p

import android.content.BroadcastReceiver
import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hanmajid.android.seed.databinding.FragmentWifiP2pBinding

class WifiP2pFragment : Fragment() {

    private lateinit var binding: FragmentWifiP2pBinding

    private val wifiP2pManager: WifiP2pManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }
    private var wifiP2pChannel: WifiP2pManager.Channel? = null
    private var wifiP2pBroadcastReceiver: BroadcastReceiver? = null

    private val adapter = WifiP2PDeviceListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWifiP2pBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        wifiP2pChannel = wifiP2pManager.initialize(requireContext(), Looper.getMainLooper(), null)
        wifiP2pChannel?.also { channel ->
            WifiP2pBroadcastReceiver(
                wifiP2pManager,
                channel,
                requireActivity(),
                viewLifecycleOwner
            ) {
                Log.wtf(
                    TAG,
                    it.toString()
                )
                adapter.submitList(it.deviceList.toList())
            }
        }
        wifiP2pManager.discoverPeers(wifiP2pChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.wtf("discoverPeers", "SUCCESS")
            }

            override fun onFailure(reason: Int) {
                Log.wtf("discoverPeers", "FAILED: $reason")
            }
        })
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
    }

    companion object {
        private const val TAG = "WifiP2pFragment"
    }
}