package com.devlog.article.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.UserPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArticleApplication : Application() {

    companion object {
        const val TAG = "Application"

        @Volatile
        lateinit var instance: ArticleApplication


    }
    init {
        instance =this

        Log.e("dfafs","safsfdsfsdfsd")


    }
    override fun onCreate() {
        super.onCreate()
        MixPanelManager.init(applicationContext)
    }

}