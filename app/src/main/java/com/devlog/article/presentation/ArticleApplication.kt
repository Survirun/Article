package com.devlog.article.presentation

import android.app.Application

class ArticleApplication : Application() {

    companion object {
        const val TAG = "Application"

        @Volatile
        lateinit var instance: ArticleApplication

        private const val INMOBIACCOUNTID="6133b31b770a4992a0def8d4adef2742"
        const val IRONSOURCE = "18d128705"
        private const  val  LIFTOFFAPPID="6508e79860aebaee1ce35046"
        private const val publisherId= "1100018571"
    }

}