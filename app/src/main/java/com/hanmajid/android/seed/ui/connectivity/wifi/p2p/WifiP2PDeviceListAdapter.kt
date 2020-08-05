package com.hanmajid.android.seed.ui.connectivity.wifi.p2p

import android.content.Context
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hanmajid.android.seed.databinding.ItemWifiP2pDeviceBinding
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants

class WifiP2PDeviceListAdapter(
    private val context: Context,
    private val manager: WifiP2pManager?,
    private val channel: WifiP2pManager.Channel?,
    private val onClickSend: () -> Unit,
    private val onClickReceive: () -> Unit
) :
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
                return oldItem.deviceAddress == newItem.deviceAddress && oldItem.status == newItem.status
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WifiP2PDeviceViewHolder).bind(
            context,
            getItem(position),
            manager,
            channel,
            onClickSend,
            onClickReceive
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WifiP2PDeviceViewHolder.create(
            parent
        )
    }

    class WifiP2PDeviceViewHolder(val binding: ItemWifiP2pDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun connectToDevice(
            manager: WifiP2pManager?,
            channel: WifiP2pManager.Channel?,
            device: WifiP2pDevice
        ) {
            val config = WifiP2pConfig()
            config.deviceAddress = device.deviceAddress
            channel?.also { channel ->
                manager?.connect(
                    channel,
                    config,
                    object : WifiP2pManager.ActionListener {
                        override fun onSuccess() {
                            Log.wtf(TAG, "Success")
                        }

                        override fun onFailure(reason: Int) {
                            Log.wtf(TAG, "Failed")
                        }

                    }
                )
            }
        }

        private fun disconnectToDevice(
            manager: WifiP2pManager?,
            channel: WifiP2pManager.Channel?,
            device: WifiP2pDevice
        ) {
            binding.isDisconnecting = true
            val config = WifiP2pConfig()
            config.deviceAddress = device.deviceAddress
            manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    binding.isDisconnecting = false
                }

                override fun onFailure(reason: Int) {
                    binding.isDisconnecting = false
                }
            })
        }

        fun bind(
            context: Context,
            device: WifiP2pDevice?,
            manager: WifiP2pManager?,
            channel: WifiP2pManager.Channel?,
            onClickSend: () -> Unit,
            onClickReceive: () -> Unit
        ) {
            device?.apply {
                binding.device = this
                binding.cardView.setOnClickListener {
                    when (this.status) {
                        WifiConstants.WIFI_P2P_DEVICE_STATUS_AVAILABLE -> {
                            MaterialAlertDialogBuilder(context)
                                .setTitle(
                                    "Connect to ${this.deviceName}?"
                                )
                                .setNegativeButton("Cancel") { _, _ ->

                                }
                                .setPositiveButton("Connect") { _, _ ->
                                    connectToDevice(manager, channel, this)
                                }
                                .show()
                        }
                    }
                }
                binding.buttonSend.setOnClickListener {
                    onClickSend()
                }
                binding.buttonReceive.setOnClickListener {
                    onClickReceive()
                }
                binding.buttonDisconnect.setOnClickListener {
                    MaterialAlertDialogBuilder(context)
                        .setTitle(
                            "Disconnect from ${this.deviceName}?"
                        )
                        .setNegativeButton("Cancel") { _, _ ->

                        }
                        .setPositiveButton("Disconnect") { _, _ ->
                            disconnectToDevice(manager, channel, this)
                        }
                        .show()
                }
            }
        }

        companion object {
            private const val TAG = "WifiP2pDeviceListAdapter"
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