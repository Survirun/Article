package com.devlog.article.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import com.devlog.article.data.preference.UserPreference

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

}