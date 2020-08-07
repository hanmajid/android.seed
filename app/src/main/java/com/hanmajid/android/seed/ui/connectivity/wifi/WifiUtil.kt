package com.hanmajid.android.seed.ui.connectivity.wifi

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_P2P_DEVICE_STATUS_AVAILABLE
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_P2P_DEVICE_STATUS_CONNECTED
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_P2P_DEVICE_STATUS_FAILED
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_P2P_DEVICE_STATUS_INVITED
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_P2P_DEVICE_STATUS_UNAVAILABLE
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_SIGNAL_LEVEL_AVERAGE
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_SIGNAL_LEVEL_BAD
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_SIGNAL_LEVEL_GOOD
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_SIGNAL_LEVEL_VERY_BAD
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants.WIFI_SIGNAL_LEVEL_VERY_GOOD

class WifiUtil {

    companion object {

        /**
         * Requires [CHANGE_WIFI_STATE] and [ACCESS_FINE_LOCATION] permission.
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
         * Requires [ACCESS_WIFI_STATE] and [ACCESS_FINE_LOCATION] permission.
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

        @JvmStatic
        fun getWifiP2PDeviceStatusDescription(status: Int) = when (status) {
            WIFI_P2P_DEVICE_STATUS_CONNECTED -> "Connected"
            WIFI_P2P_DEVICE_STATUS_INVITED -> "Invited"
            WIFI_P2P_DEVICE_STATUS_FAILED -> "Failed"
            WIFI_P2P_DEVICE_STATUS_AVAILABLE -> "Available"
            WIFI_P2P_DEVICE_STATUS_UNAVAILABLE -> "Unavailable"
            else -> "-"
        }

        @JvmStatic
        fun getWifiStateDescription(state: Int) = when (state) {
            WifiManager.WIFI_STATE_DISABLED -> "Disabled"
            WifiManager.WIFI_STATE_DISABLING -> "Disabling"
            WifiManager.WIFI_STATE_ENABLED -> "Enabled"
            WifiManager.WIFI_STATE_ENABLING -> "Enabling"
            WifiManager.WIFI_STATE_UNKNOWN -> "Unknown"
            else -> ""
        }

        @JvmStatic
        fun getWifiP2PFailureReason(reason: Int) = when (reason) {
            WifiP2pManager.ERROR -> "Error"
            WifiP2pManager.P2P_UNSUPPORTED -> "P2P unsupported"
            WifiP2pManager.BUSY -> "Busy"
            WifiP2pManager.NO_SERVICE_REQUESTS -> "No service requests"
            else -> ""
        }

        /**
         * Requires [ACCESS_FINE_LOCATION] permission.
         */
        @JvmStatic
        fun getCurrentlyConnectedWifiInfo(wifiManager: WifiManager): WifiInfo {
            return wifiManager.connectionInfo
        }

        fun getWifiSuggestionStatusDescription(status: Int) = when(status) {
            WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS -> "Success"
            WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL -> "Error: Internal error :("
            WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED -> "Error: Please allow this app to control Wi-Fi"
            WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE -> "Error: This suggestion is duplicate"
            WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP -> "Error: Max suggestions is exceeded. Consider removing unused suggestions."
            WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID -> "Error: This suggestion doesn't exist."
            else -> ""
        }
    }
}