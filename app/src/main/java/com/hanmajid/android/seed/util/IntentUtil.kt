package com.hanmajid.android.seed.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

/**
 * Utility class for handling intents.
 */
object IntentUtil {

    /**
     * Start an implicit intent securely based on Android security's best practice.
     *
     * @param activity Activity.
     * @param intent Intent that wants to be started.
     * @param useAppChooserIfPossible Whether to show App Chooser if there are multiple
     *          apps that can handle the intent. Based on Android security's best practices,
     *          this behavior is true by default, but can be turned off if needed.
     * @param chooserTitle The string displayed in App Chooser.
     *
     * @see <a href="https://developer.android.com/topic/security/best-practices#show-an-app-chooser">Security: Show an app chooser</a>
     */
    fun startImplicitIntent(
        activity: Activity,
        intent: Intent,
        useAppChooserIfPossible: Boolean = true,
        chooserTitle: String? = null
    ) {
        val packageManager = activity.packageManager

        val possibleActivitesList: List<ResolveInfo> =
            packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_ALL
            )

        // Verify that an activity in at least two apps on the user's device
        // can handle the intent. Otherwise, start the intent only if an app
        // on the user's device can handle the intent.
        if (useAppChooserIfPossible && possibleActivitesList.size > 1) {
            // Create intent to show chooser.
            val chooser = Intent.createChooser(intent, chooserTitle)
            activity.startActivity(chooser)
        } else if (intent.resolveActivity(packageManager) != null) {
            activity.startActivity(intent)
        }
    }
}