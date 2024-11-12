package com.devlog.article.presentation.my_keywords_select

import com.devlog.article.presentation.splash.SplashIntent

sealed class MyKeywordSelectIntent {
    data class postMyKeywordSelectPost(val keywords: Array<Int>) : MyKeywordSelectIntent()
}