package com.hanmajid.android.seed.util

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class PermissionUtil {

    companion object {

        @JvmStatic
        fun allPermissionsGranted(
            context: Context,
            permissions: Array<String>
        ): Boolean {
            return permissions.all {
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        @JvmStatic
        fun requestAllPermissions(
            fragment: Fragment,
            view: View,
            permissions: Array<String>,
            requestCode: Int
        ) {
            val provideRationale =
                allPermissionsGranted(
                    fragment.requireContext(),
                    permissions
                )

            if (provideRationale) {
                Snackbar.make(
                    view,
                    "You should grant this permission",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("OK") {
                        // Request permission
                        fragment.requestPermissions(
                            permissions,
                            requestCode
                        )
                    }
                    .show()
            } else {
                fragment.requestPermissions(
                    permissions,
                    requestCode
                )
            }
        }
    }
}