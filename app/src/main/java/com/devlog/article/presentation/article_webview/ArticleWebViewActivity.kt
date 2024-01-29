package com.devlog.article.presentation.article_webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devlog.article.R
import com.devlog.article.databinding.ActivityArticleWebViewBinding

class ArticleWebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityArticleWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getStringExtra("url")
        binding.webView.loadUrl( intent.getStringExtra("url")!!)


    }
}