package com.devlog.article.presentation.article_webview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.devlog.article.data.entity.naver.ApiData
import com.devlog.article.data.entity.naver.Document
import com.devlog.article.data.entity.naver.OptionObject
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.databinding.ActivityArticleWebViewBinding
import com.google.android.material.snackbar.Snackbar

class ArticleWebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityArticleWebViewBinding
    lateinit var articleWebViewModel: ArticleWebViewModel
    var body = ""
    lateinit var postTextSummary :()->Unit
    var url =""
    lateinit var articleGetbody:ArticleGetbody
    lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url=intent.getStringExtra("url")!!
        initWebView()
        userPreference=UserPreference.getInstance(this)
        articleWebViewModel = ArticleWebViewModel()
        articleGetbody = ArticleGetbody(binding.webView)
        userPreference.coin+=1
        Snackbar.make(binding.root, "기사를 읽어 1 코인을 얻었어요!", Snackbar.LENGTH_SHORT).show()
        postTextSummary={
            cutString()
            articleWebViewModel.textSummary(ApiData(Document(title = "", content = body)))
        }

        articleWebViewModel.succeed ={
            binding.textView.text = it
        }
        //shareLink()



    }
    private fun cutString(){
        val maxLength = 2000

        body = if (body.length > maxLength) {
            body.substring(0, maxLength)
        } else {
            body
        }
    }

    private fun shareLink(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
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



    class MyJavascriptInterface(var activity: ArticleWebViewActivity) {
        @JavascriptInterface
        fun getHtml(html: String) {

            activity.body += html.replace("\n","")
            activity.postTextSummary()


            Log.d("Test", "html: $html")
        }


    }
}