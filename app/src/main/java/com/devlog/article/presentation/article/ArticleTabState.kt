package com.devlog.article.presentation.article

import com.devlog.article.data.response.Article

data class ArticleTabState(
    var articles: ArrayList<Article> = ArrayList(),
    val keyword : Int,
    val maxPage : Int,
    var page: Int = 1
)