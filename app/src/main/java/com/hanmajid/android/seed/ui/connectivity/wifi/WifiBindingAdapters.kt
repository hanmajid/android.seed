package com.hanmajid.android.seed.ui.connectivity.wifi

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlin.math.floor

@BindingAdapter("wifiSignalLevel")
fun setWifiSignalLevel(textView: TextView?, scanResult: ScanResult?) {
    textView?.apply {
        scanResult?.let {
            val level =
                WifiManager.calculateSignalLevel(it.level, WifiUtil.WIFI_SIGNAL_LEVEL_VERY_GOOD + 1)
            text = WifiUtil.getWifiSignalLevelDescription(level)
        }
    }
}

@BindingAdapter("wifiFrequency")
fun setWifiFrequency(textView: TextView?, scanResult: ScanResult?) {
    textView?.apply {
        scanResult?.let {
            val x = floor(it.frequency.toDouble() / 100)
            val freq = String.format("%.1f", x / 10)
            text = "$freq GHz"
        }
    }
}