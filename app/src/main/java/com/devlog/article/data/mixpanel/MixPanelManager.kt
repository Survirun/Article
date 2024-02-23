package com.devlog.article.data.mixpanel

import android.annotation.SuppressLint
import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI


@SuppressLint("StaticFieldLeak")
object MixPanelManager {

    private lateinit var context: Context
    private lateinit var mixpanel: Mixpanel

    fun init(mContext: Context) {
        context = mContext
        mixpanel = Mixpanel(mContext)
    }


    fun articleClick(title: String) {
        mixpanel.mixpanelEvent("article_click", mapOf("value" to title))
    }
    fun articleShare(title: String) {
        mixpanel.mixpanelEvent("article_share", mapOf("value" to title))
    }

    fun articleBookmark(title: String){
        mixpanel.mixpanelEvent("article_bookmark", mapOf("value" to title))
    }


}