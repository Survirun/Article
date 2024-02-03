package com.devlog.article.presentation.article_webview

import android.annotation.SuppressLint
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
import com.devlog.article.databinding.ActivityArticleWebViewBinding

class ArticleWebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityArticleWebViewBinding
    lateinit var articleWebViewModel: ArticleWebViewModel
    var body = ""
    lateinit var postTextSummary :()->Unit
    var url =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url=intent.getStringExtra("url")!!
        binding.webView.settings.run {
            javaScriptEnabled = true;
            if (url.contains("yozm.wishket")){
                javaScriptEnabled = false;
            }
             // 웹페이지 자바스클비트 허용 여부
            setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
            databaseEnabled = true; // 데이터베이스 접근 허용 여부
        }
        binding.webView.addJavascriptInterface(MyJavascriptInterface(this), "Android")
        binding.webView.loadUrl(intent.getStringExtra("url")!!)
        articleWebViewModel = ArticleWebViewModel()

        var articleGetbody = ArticleGetbody(binding.webView)


        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                articleGetbody.getBody(binding.webView.url!!)
                // view?.loadUrl("javascript:window.Android.getHtmlSize(document.getElementsByClassName(\"sticky-article\")[0].getElementsByTagName(\"p\").length)")
                // view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByClassName(\"sticky-article\")[0].getElementsByTagName(\"p\")[0].innerHTML)")

                // <html></html> 사이에 있는 html 소스를 넘겨준다.
            }
        }


        //https://stackoverflow.com/questions/9579772/android-get-text-out-of-webview

        postTextSummary={
            val maxLength = 2000

             body = if (body.length > maxLength) {
                body.substring(0, maxLength)
            } else {
                body
            }
            articleWebViewModel.textSummary(
                ApiData(
                    document =
                    Document(
                        title = "",
                        content = body
                    ), option = OptionObject()
                )
            )
        }

        articleWebViewModel.succeed ={
            binding.textView.text = it
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