package com.devlog.article.presentation.article_webview

import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.devlog.article.databinding.ActivityArticleWebViewBinding
import com.devlog.article.utility.isProbablyKorean
import com.devlog.article.utility.shareLink


class ArticleWebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityArticleWebViewBinding
    lateinit var articleWebViewModel: ArticleWebViewModel
    var body = ""

    var url =""
    var title=""
    lateinit var articleGetbody:ArticleGetbody
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url=intent.getStringExtra("url")!!
        title=intent.getStringExtra("title")!!
        if (!title.isProbablyKorean()){
            val url = url
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, Uri.parse(url))
        }
        initWebView()
        initToolBar()
        articleWebViewModel = ArticleWebViewModel()
        articleGetbody = ArticleGetbody(binding.webView)





        binding.shareButton.setOnClickListener {
            shareLink(url)
        }



    }
    private fun cutString(){
        val maxLength = 2000

        body = if (body.length > maxLength) {
            body.substring(0, maxLength)
        } else {
            body
        }
    }


    private fun initWebView(){
        binding.webView.settings.run {
            if (!url.contains("medium.com")){
                javaScriptEnabled = true;
            }

            if (url.contains("yozm.wishket")){
                javaScriptEnabled = false;
            }
            // 웹페이지 자바스클비트 허용 여부
            setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
            databaseEnabled = true; // 데이터베이스 접근 허용 여부
        }
        binding.webView.addJavascriptInterface(MyJavascriptInterface(this), "Android")
        binding.webView.loadUrl(url)

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

               // articleGetbody.getBody(binding.webView.url!!)

            }
        }
    }

    private fun initToolBar(){
        binding.textView.movementMethod = ScrollingMovementMethod()
        binding.aiButton.setOnCheckedChangeListener { _, isChecked ->
            binding.textView.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
        }

        binding.webView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val contentHeight = binding.webView.contentHeight * binding.webView.scale
            val webViewHeight = binding.webView.height.toFloat()
            val scrollPercentage = ((scrollY / (contentHeight - webViewHeight)) * 100).toInt() + 1
            binding.progressBar.progress = scrollPercentage
        }
    }



    class MyJavascriptInterface(var activity: ArticleWebViewActivity) {
        @JavascriptInterface
        fun getHtml(html: String) {

            activity.body += html.replace("\n","")


            Log.d("Test", "html: $html")
        }


    }
}