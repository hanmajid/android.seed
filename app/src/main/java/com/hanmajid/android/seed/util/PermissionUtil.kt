package com.hanmajid.android.seed.util

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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

        @JvmStatic
        fun requestPermission(
            fragment: Fragment,
            permission: String,
            onResult: (Boolean) -> Unit
        ) {
            val contract =
                fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    onResult(it)
                }
            contract.launch(permission)
        }

        fun requestPermissionsIfNeeded(
            fragment: Fragment,
            permissions: Array<String>,
            onResult: (Map<String, Boolean>?) -> Unit
        ) {
            if (allPermissionsGranted(fragment.requireContext(), permissions)) {
                onResult(null)
            } else {
                val provideRationale = permissions.any {
                    fragment.shouldShowRequestPermissionRationale(it)
                }
                if (provideRationale) {
                    Snackbar.make(
                        fragment.requireView(),
                        "Please grant permissions for this app.",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("OK") {
                            // Request permission
                            requestPermissions(
                                fragment,
                                permissions,
                                onResult
                            )
                        }
                        .show()
                } else {
                    // Request permission
                    requestPermissions(
                        fragment,
                        permissions,
                        onResult
                    )
                }
            }
        }

        @JvmStatic
        fun requestPermissions(
            fragment: Fragment,
            permissions: Array<String>,
            onResult: (Map<String, Boolean>) -> Unit
        ) {
            val contract =
                fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    onResult(it)
                }
            contract.launch(permissions)
        }
    }
}