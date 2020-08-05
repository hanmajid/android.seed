package com.hanmajid.android.seed.ui.connectivity.wifi.p2p

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class WifiP2pBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    private val onWifiStateChanged: (Int) -> Unit,
    private val onUpdatePeers: (WifiP2pDeviceList) -> Unit,
    private val onConnectionChanged: (WifiP2pInfo?, WifiP2pGroup?) -> Unit
) : LifecycleObserver {

    private lateinit var receiver: BroadcastReceiver

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerBroadcast() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                        Log.wtf(TAG, "WIFI_P2P_STATE_CHANGED_ACTION")
                        // Check to see if Wi-Fi is enabled and notify appropriate activity
                        val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                        onWifiStateChanged(state)
                    }
                    WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                        // Call WifiP2pManager.requestPeers() to get a list of current peers
                        Log.wtf(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION")
                        manager.requestPeers(channel) { peers ->
                            onUpdatePeers(peers)
                        }
                    }
                    WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                        // Respond to new connection or disconnections
                        Log.wtf(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION")
//                        manager.requestPeers(channel) { peers ->
//                            onUpdatePeers(peers)
//                        }
                        val wifiP2pInfo =
                            intent.getParcelableExtra<WifiP2pInfo>(WifiP2pManager.EXTRA_WIFI_P2P_INFO)
                        val wifiP2pGroup =
                            intent.getParcelableExtra<WifiP2pGroup>(WifiP2pManager.EXTRA_WIFI_P2P_GROUP)
                        Log.wtf("wifiP2pInfo", wifiP2pInfo?.toString())
                        Log.wtf("wifiP2pGroup", wifiP2pGroup?.toString())
                        onConnectionChanged(wifiP2pInfo, wifiP2pGroup)
                    }
                    WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                        // Respond to this device's wifi state changing
                        Log.wtf(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION")
//                        manager.requestPeers(channel) { peers ->
//                            onUpdatePeers(peers)
//                        }
                    }
                }
            }
        }
        val intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
        activity.registerReceiver(receiver, intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterBroadcast() {
        activity.unregisterReceiver(receiver)
    }

    companion object {
        private const val TAG = "WifiP2pBroadcastReceiver"
    }
}