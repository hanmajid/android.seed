package com.hanmajid.android.seed.ui.connectivity.wifi.suggestion

import android.net.wifi.WifiManager
import android.os.Build
import androidx.fragment.app.Fragment
import com.hanmajid.android.seed.ui.connectivity.wifi.WifiConstants
import com.hanmajid.android.seed.util.PermissionUtil

class WifiSuggestionUtil {
    companion object {
        @JvmStatic
        fun addWifiNetworkSuggestions(
            fragment: Fragment,
            wifiManager: WifiManager,
            suggestions: List<MyWifiSuggestion>,
            onSuccess: (status: Int) -> Unit,
            onError: (status: Int) -> Unit
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val wifiSuggestions = suggestions.map { it.toNetworkSuggestion() }
                // Ask permission
                val permissions = arrayOf(android.Manifest.permission.CHANGE_WIFI_STATE)
                if (PermissionUtil.allPermissionsGranted(
                        fragment.requireContext(),
                        permissions
                    )
                ) {
                    onSuccess(wifiManager.addNetworkSuggestions(wifiSuggestions))
                } else {
                    PermissionUtil.requestPermission(
                        fragment,
                        permissions[0]
                    ) { isGranted ->
                        if (isGranted) {
                            onSuccess(wifiManager.addNetworkSuggestions(wifiSuggestions))
                        } else {
                            // Permission is not granted.
                            onError(WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_PERMISSIONS)
                        }
                    }
                }
            } else {
                // API level is not sufficient.
                onError(WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_API_LEVEL)
            }
        }

        @JvmStatic
        fun removeWifiNetworkSuggestions(
            fragment: Fragment,
            wifiManager: WifiManager,
            suggestions: List<MyWifiSuggestion>,
            onSuccess: (status: Int) -> Unit,
            onError: (status: Int) -> Unit
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val wifiSuggestions = suggestions.map { it.toNetworkSuggestion() }
                // Ask permission
                val permissions = arrayOf(android.Manifest.permission.CHANGE_WIFI_STATE)
                if (PermissionUtil.allPermissionsGranted(
                        fragment.requireContext(),
                        permissions
                    )
                ) {
                    onSuccess(wifiManager.removeNetworkSuggestions(wifiSuggestions))
                } else {
                    PermissionUtil.requestPermission(
                        fragment,
                        permissions[0]
                    ) { isGranted ->
                        if (isGranted) {
                            onSuccess(wifiManager.removeNetworkSuggestions(wifiSuggestions))
                        } else {
                            // Permission is not granted.
                            onError(WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_PERMISSIONS)
                        }
                    }
                }
            } else {
                // API level is not sufficient.
                onError(WifiConstants.STATUS_NETWORK_SUGGESTIONS_ERROR_API_LEVEL)
            }
        }
    }
}