package com.hanmajid.android.seed.util

import android.util.Patterns

class StringUtil {
    companion object {
        @JvmStatic
        fun isValidUrl(url: String): Boolean {
            return Patterns.WEB_URL.matcher(url).matches()
        }
    }
}