package com.hanmajid.android.seed.ui.connectivity.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class WifiStateBroadcastListener(
    lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val onWifiStateChanged: (prevState: Int, state: Int) -> Unit,
    private val onConnectionChanged: () -> Unit
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
                    WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                        val previousWifiState =
                            intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, -1)
                        val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)
                        onWifiStateChanged(previousWifiState, wifiState)
                    }
                    WIFI_STATE_CONNECTION_CHANGES_ACTION -> {
                        onConnectionChanged()
                    }
                }
            }
        }
        val intentFilter = IntentFilter().apply {
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            addAction(WIFI_STATE_CONNECTION_CHANGES_ACTION)
        }
        context.registerReceiver(receiver, intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterBroadcast() {
        context.unregisterReceiver(receiver)
    }

    companion object {
        const val WIFI_STATE_CONNECTION_CHANGES_ACTION = "WIFI_STATE_CONNECTION_CHANGES_ACTION"
    }
}