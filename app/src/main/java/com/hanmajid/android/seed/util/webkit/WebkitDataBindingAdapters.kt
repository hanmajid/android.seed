package com.hanmajid.android.seed.util.webkit

import android.util.Base64
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import com.hanmajid.android.seed.util.StringUtil

const val TAG = "WebkitDataBindingAdapters"

@BindingAdapter(value = ["webViewLoadUrl", "webViewClient"])
fun setWebViewLoadUrl(
    webView: WebView?,
    url: String?,
    webViewClient: WebViewClient?
) {
    webView?.let { view ->
        webViewClient?.let { client ->
            url?.let {
                if (StringUtil.isValidUrl(it)) {
                    view.webViewClient = client
                    view.loadUrl(it)
                } else {
                    Log.e(TAG, "Not a valid URL: $url")
                }
            }
        }
    }
}

@BindingAdapter("webViewLoadData")
fun setWebViewLoadData(webView: WebView?, html: String?) {
    webView?.let { view ->
        html?.let {
            val encodedHtml = Base64.encodeToString(it.toByteArray(), Base64.NO_PADDING)
            view.loadData(encodedHtml, "text/html", "base64")
        }
    }
}

@BindingAdapter("webViewLoadDataWithBaseUrl")
fun setWebViewLoadDataWithBaseUrl(webView: WebView?, model: WebKitLoadDataWithBaseUrlModel?) {
    webView?.let { view ->
        model?.let {
            val encodedHtml = Base64.encodeToString(it.html.toByteArray(), Base64.NO_PADDING)
            view.loadDataWithBaseURL(it.baseUrl, encodedHtml, "text/html", "base64", null)
        }
    }
}