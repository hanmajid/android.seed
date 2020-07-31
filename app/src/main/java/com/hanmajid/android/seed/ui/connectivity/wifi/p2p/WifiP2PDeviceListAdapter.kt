package com.hanmajid.android.seed.ui.connectivity.wifi.p2p

import android.net.wifi.p2p.WifiP2pDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hanmajid.android.seed.databinding.ItemWifiP2pDeviceBinding

class WifiP2PDeviceListAdapter :
    ListAdapter<WifiP2pDevice, RecyclerView.ViewHolder>(WIFI_COMPARATOR) {

    companion object {
        private val WIFI_COMPARATOR = object : DiffUtil.ItemCallback<WifiP2pDevice>() {
            override fun areItemsTheSame(oldItem: WifiP2pDevice, newItem: WifiP2pDevice): Boolean {
                return oldItem.deviceAddress == newItem.deviceAddress
            }

            override fun areContentsTheSame(
                oldItem: WifiP2pDevice,
                newItem: WifiP2pDevice
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WifiP2PDeviceViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WifiP2PDeviceViewHolder.create(
            parent
        )
    }

    class WifiP2PDeviceViewHolder(val binding: ItemWifiP2pDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: WifiP2pDevice?) {
            device?.apply {
                binding.device = this
            }
        }

        companion object {
            fun create(parent: ViewGroup): WifiP2PDeviceViewHolder {
                val binding = ItemWifiP2pDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return WifiP2PDeviceViewHolder(
                    binding
                )
            }
        }
    }
}