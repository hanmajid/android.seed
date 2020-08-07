package com.hanmajid.android.seed.ui.connectivity.wifi.suggestion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class WifiSuggestionConnectedBroadcastListener(
    lifecycleOwner: LifecycleOwner,
    val context: Context,
    val onSuccess: () -> Unit,
    val onError: () -> Unit
) : LifecycleObserver {
    private lateinit var receiver: BroadcastReceiver

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerBroadcast() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.action == WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION) {
                    onSuccess()
                }
            }
        }
        val intentFilter = IntentFilter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intentFilter.addAction(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)
        } else {
            onError()
        }
        context.registerReceiver(receiver, intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterBroadcast() {
        context.unregisterReceiver(receiver)
    }
}