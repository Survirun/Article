package com.devlog.feature_my_keywords_select.intent

sealed class MyKeywordSelectIntent {
    data class postMyKeywordSelectPost(val keywords: Array<Int>) : MyKeywordSelectIntent()
}