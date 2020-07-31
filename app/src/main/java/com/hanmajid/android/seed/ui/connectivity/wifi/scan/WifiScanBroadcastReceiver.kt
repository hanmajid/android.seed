package com.hanmajid.android.seed.ui.connectivity.wifi.scan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiUtil

class WifiScanBroadcastReceiver(
    lifecycleOwner: LifecycleOwner,
    val context: Context,
    val wifiManager: WifiManager,
    val distinctSSID: Boolean,
    val scanSuccess: (scanResults: List<ScanResult>) -> Unit,
    val scanFailure: (scanResults: List<ScanResult>) -> Unit
) : LifecycleObserver {

    private lateinit var receiver: BroadcastReceiver

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerBroadcast() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent?.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED,
                        false
                    )
                } else {
                    true
                }
                if (success == true) {
                    scanSuccess(
                        WifiUtil.getScanResults(
                            wifiManager,
                            distinctSSID
                        )
                    )
                } else {
                    scanFailure(
                        WifiUtil.getScanResults(
                            wifiManager,
                            distinctSSID
                        )
                    )
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(receiver, intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterBroadcast() {
        context.unregisterReceiver(receiver)
    }
}