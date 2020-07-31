package com.hanmajid.android.seed.ui.connectivity.wifi

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pDevice
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_SIGNAL_LEVEL_VERY_GOOD
import kotlin.math.floor

@BindingAdapter("wifiP2pDeviceStatus")
fun setWifiP2pDeviceStatus(textView: TextView?, device: WifiP2pDevice?) {
    textView?.apply {
        device?.let {
            text = WifiUtil.getWifiP2PDeviceStatusDescription(it.status)
        }
    }
}

@BindingAdapter("wifiSignalLevel")
fun setWifiSignalLevel(textView: TextView?, scanResult: ScanResult?) {
    textView?.apply {
        scanResult?.let {
            val level =
                WifiManager.calculateSignalLevel(it.level, WIFI_SIGNAL_LEVEL_VERY_GOOD + 1)
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