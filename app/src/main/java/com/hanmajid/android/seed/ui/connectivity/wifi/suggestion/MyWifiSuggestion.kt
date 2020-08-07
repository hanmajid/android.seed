package com.hanmajid.android.seed.ui.connectivity.wifi.suggestion

import android.net.wifi.WifiNetworkSuggestion
import android.os.Build

data class MyWifiSuggestion(
    val ssid: String,
    val bssid: String? = null,
    val isAppInteractionRequired: Boolean? = null,
    val priority: Int? = null,
    val wpa2Passphrase: String? = null,
    val wpa3Passphrase: String? = null
) {
    val hasPassword: Boolean = wpa2Passphrase != null || wpa3Passphrase != null
}

fun MyWifiSuggestion.toNetworkSuggestion(): WifiNetworkSuggestion? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val builder = WifiNetworkSuggestion.Builder().apply {
            setSsid(ssid)
            wpa2Passphrase?.let {
                setWpa2Passphrase(it)
            }
            wpa3Passphrase?.let {
                setWpa3Passphrase(it)
            }
            isAppInteractionRequired?.let {
                setIsAppInteractionRequired(it)
            }
        }
        return builder.build()
    } else {
        return null
    }

}