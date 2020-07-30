package com.hanmajid.android.seed.ui.connectivity.wifi

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager

class WifiUtil {

    companion object {

        const val WIFI_SIGNAL_LEVEL_VERY_BAD = 0
        const val WIFI_SIGNAL_LEVEL_BAD = 1
        const val WIFI_SIGNAL_LEVEL_AVERAGE = 2
        const val WIFI_SIGNAL_LEVEL_GOOD = 3
        const val WIFI_SIGNAL_LEVEL_VERY_GOOD = 4

        /**
         * Requires [CHANGE_WIFI_STATE] permission.
         */
        @JvmStatic
        fun startScanning(
            wifiManager: WifiManager,
            distinctSSID: Boolean,
            scanFailure: (scanResults: List<ScanResult>) -> Unit
        ) {
            val success = wifiManager.startScan()
            if (!success) {
                scanFailure(getScanResults(wifiManager, distinctSSID))
            }
        }

        /**
         * Requires [ACCESS_WIFI_STATE] permission.
         */
        @JvmStatic
        fun getScanResults(wifiManager: WifiManager, distinctSSID: Boolean): List<ScanResult> {
            return if (distinctSSID) filterDistinctSSID(wifiManager.scanResults) else wifiManager.scanResults
        }

        @JvmStatic
        fun filterDistinctSSID(scanResults: List<ScanResult>): List<ScanResult> {
            val distinctResults = mutableMapOf<String, ScanResult>()
            scanResults.forEach {
                if (!distinctResults.contains(it.SSID)) {
                    distinctResults[it.SSID] = it
                } else if (distinctResults[it.SSID]!!.level < it.level) {
                    distinctResults[it.SSID] = it
                }
            }
            return distinctResults.values.toList()
        }

        @JvmStatic
        fun getWifiSignalLevelDescription(levelNum: Int) = when (levelNum) {
            WIFI_SIGNAL_LEVEL_VERY_BAD -> "Very Bad"
            WIFI_SIGNAL_LEVEL_BAD -> "Bad"
            WIFI_SIGNAL_LEVEL_AVERAGE -> "Average"
            WIFI_SIGNAL_LEVEL_GOOD -> "Good"
            WIFI_SIGNAL_LEVEL_VERY_GOOD -> "Very Good"
            else -> "-"
        }
    }
}