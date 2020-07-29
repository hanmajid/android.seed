package com.hanmajid.android.seed.ui.connectivity.wifi

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hanmajid.android.seed.databinding.FragmentWifiBinding

class WifiFragment : Fragment() {

    private lateinit var binding: FragmentWifiBinding
    private lateinit var wifiManager: WifiManager

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
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter

        // Step 1. Get WifiManager from systemService.
        wifiManager = requireContext().getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Step 2. Instantiate [WifiScanBroadcastReceiver]
        WifiScanBroadcastReceiver(
            viewLifecycleOwner,
            requireContext(),
            wifiManager,
            true,
            { scanResults ->
                Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
                adapter.submitList(scanResults)
            }, { scanResults ->
                Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
                adapter.submitList(scanResults)
            }
        )

        // Step 3. Set listener for rescanning.
        binding.swipeRefresh.setOnRefreshListener {
            WifiUtil.startScanning(wifiManager, true) { scanResults ->
                Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
                adapter.submitList(scanResults)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    companion object {
        private const val TAG = "WifiFragment"
    }
}