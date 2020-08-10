package com.hanmajid.android.seed.ui.userguide

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.ActivityUserGuideBinding
import com.hanmajid.android.seed.util.webkit.WebKitLoadDataWithBaseUrlModel

class UserGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_guide)
        setupBinding()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
//        binding.webViewClient = WebViewClient()
        binding.model = WebKitLoadDataWithBaseUrlModel(
            baseUrl = "data://",
            html = "<html>" +
                    "<body>" +
                    "<h2>Hello world!</h2>" +
                    "<p>This is android.seed !</p>" +
                    "<a href=\"https://www.hanmajid.com\">Go to hanmajid</a>" +
                    "<a href=\"https://developer.android.com\">Go to Android documentation</a>" +
                    "</body>" +
                    "</html>"
        )
        binding.url = "https://google.com"
        binding.html = "<html>" +
                "<body>" +
                "<h2>Hello world!</h2>" +
                "<p>This is android.seed !</p>" +
                "</body>" +
                "</html>"

        binding.webView.webViewClient = MyWebViewClient(this)

        // WebSettings
        binding.webView.settings.apply {
            javaScriptEnabled = true
            userAgentString = "com.hanmajid.android.seed"
        }

        // Binding JavaScript to Android code.
        binding.webView.addJavascriptInterface(WebAppInterface(this), "Android")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    class MyWebViewClient(
        private val context: Context
//        private val progressBar: ProgressIndicator
    ) : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url?.host == "www.hanmajid.com") {
                // Let WebView load the page.
                return false
            }
            // Launch another Activity that handles URLs.
            Intent(Intent.ACTION_VIEW, request?.url).apply {
                context.startActivity(this)
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//            progressBar.show()
            return super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
//            progressBar.hide()
            return super.onPageFinished(view, url)
        }
    }

    class WebAppInterface(private val context: Context) {
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
        }
    }
}