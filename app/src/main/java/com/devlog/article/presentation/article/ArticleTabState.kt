package com.devlog.article.presentation.article

import com.devlog.article.data.response.Article

data class ArticleTabState(
    var articles: ArrayList<Article> = ArrayList(),
    val keyword : Int =0,
    val maxPage : Int=0,
    var page: Int = 1
)