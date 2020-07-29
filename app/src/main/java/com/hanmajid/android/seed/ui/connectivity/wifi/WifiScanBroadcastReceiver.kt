package com.hanmajid.android.seed.ui.connectivity.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class WifiScanBroadcastReceiver(
    lifecycleOwner: LifecycleOwner,
    val context: Context,
    val wifiManager: WifiManager,
    val distinctSSID: Boolean,
    val scanSuccess: (scanResults: List<ScanResult>) -> Unit,
    val scanFailure: (scanResults: List<ScanResult>) -> Unit
) : LifecycleObserver {

    private lateinit var wifiScanReceiver: BroadcastReceiver

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerBroadcast() {
        wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val success = intent?.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED,
                    false
                )
                if (success == true) {
                    scanSuccess(WifiUtil.getScanResults(wifiManager, distinctSSID))
                } else {
                    scanFailure(WifiUtil.getScanResults(wifiManager, distinctSSID))
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)

        WifiUtil.startScanning(wifiManager, distinctSSID, scanFailure)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterBroadcast() {
        context.unregisterReceiver(wifiScanReceiver)
    }
}