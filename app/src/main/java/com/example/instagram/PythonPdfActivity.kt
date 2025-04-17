package com.example.instagram

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class PythonPdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_python_pdf)

        val webView = findViewById<WebView>(R.id.pdfWebView)
        val pdfUrl = intent.getStringExtra("pdfUrl") ?: return

        val viewerUrl = "https://drive.google.com/viewerng/viewer?embedded=true&url=$pdfUrl"

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(viewerUrl)
    }
}
