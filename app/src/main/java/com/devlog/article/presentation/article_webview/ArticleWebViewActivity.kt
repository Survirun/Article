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
import androidx.core.view.isVisible
import com.devlog.article.data.entity.naver.ApiData
import com.devlog.article.data.entity.naver.Document
import com.devlog.article.data.entity.naver.OptionObject
import com.devlog.article.databinding.ActivityArticleWebViewBinding
import com.devlog.article.utility.isProbablyKorean
import com.devlog.article.utility.shareLink


class ArticleWebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityArticleWebViewBinding
    lateinit var articleWebViewModel: ArticleWebViewModel
    lateinit var postTextSummary :()->Unit
    var body = ""

    var url =""
    var title=""
    lateinit var articleGetbody:ArticleGetbody
    val resultList = mutableListOf<String>()
    var resultAi = ""
    val chunkSize = 1000
    var aiCount =0
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

        postTextSummary={

            resultList.forEach{
                articleWebViewModel.textSummary(
                    ApiData(
                        document =
                        Document(
                            title = title,
                            content = it
                        ), option = OptionObject()
                    )
                )

            }


        }

        articleWebViewModel.succeed ={
            aiCount++
            resultAi+=it
            Log.e("fasfsadsf",it)
            if (aiCount==resultList.size){
                binding.textView.isVisible=true
                binding.textView.text = resultAi
            }

        }


    }
    fun cutString(){
        var startIndex = 0
        var endIndex = startIndex + chunkSize

        while (startIndex < body.length) {
            if (endIndex > body.length) {
                endIndex = body.length
            }
            resultList.add(body.substring(startIndex, endIndex))
            startIndex = endIndex
            endIndex = startIndex + chunkSize
        }
        postTextSummary()



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

                articleGetbody.getBody(binding.webView.url!!)

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
            activity.cutString()



            Log.d("Test", "html: ${activity.body}")
        }


    }



}