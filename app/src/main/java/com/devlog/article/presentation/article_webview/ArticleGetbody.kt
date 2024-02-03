package com.devlog.article.presentation.article_webview

import android.util.Log
import android.webkit.WebView

class ArticleGetbody(var webView: WebView) {
    fun getBody(url:String){

        when{

            url.contains("aitimes")->{
                webView.loadUrl("javascript:window.Android.getHtml(document.getElementsByClassName(\"sticky-article\")[0].innerText)")

            }
            url.contains("itworld")->{
                webView.loadUrl("javascript:window.Android.getHtml(  document.getElementsByClassName(\"node-body\")[0].innerText)")

            }
            url.contains("yozm.wishket")->{
                webView.loadUrl("javascript:window.Android.getHtml(document.getElementsByClassName(\" next-news-contents news-highlight-box\")[0].innerText)")

            }
            url.contains("velog.io")->{
                webView.loadUrl("javascript:window.Android.getHtml(document.getElementsByClassName(\"sc-brSvTw hRJeFN\")[0].innerText)")

            }
            url.contains(" mk.co.kr")->{
                webView.loadUrl("javascript:window.Android.getHtml(document.getElementsByClassName(\"sc-brSvTw hRJeFN\")[0].innerText)")

            }
            url.contains(" mk.co.kr")->{
                webView.loadUrl("javascript:window.Android.getHtml(document.getElementsByClassName(\"news_cnt_detail_wrap\")[0].innerText)")

            }




            else->{
                Log.e("아님","html")
            }
        }

        webView.settings.run {
            javaScriptEnabled = false
        }
    }
}