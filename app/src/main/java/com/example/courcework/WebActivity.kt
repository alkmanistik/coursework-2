package com.example.courcework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.courcework.databinding.ActivityMenuBinding
import com.example.courcework.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    private var onCatalog: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pageText = intent.getStringExtra("pageText")
        val webLink = intent.getStringExtra("webLink")

        binding.textPage.text = pageText
        binding.webview.settings.javaScriptEnabled = true

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // Здесь можно выполнить дополнительные действия после загрузки страницы
            }
        }

        binding.webview.loadUrl(webLink!!)

        binding.buttonBack.setOnClickListener{
            onBackPressed()
        }

    }
}