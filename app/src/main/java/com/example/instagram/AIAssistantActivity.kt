package com.example.instagram

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class AIAssistantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aiassistant)

        val webView: WebView = findViewById(R.id.aiWebView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // You can change this to Gemini, Claude, or custom URL later
        webView.loadUrl("https://www.perplexity.ai/")
    }
}
