package com.devlog.article.presentation.my_keywords_select

sealed class MyKeywordSelectIntent {
    data class postMyKeywordSelectPost(val keywords: Array<Int>) : MyKeywordSelectIntent()
}