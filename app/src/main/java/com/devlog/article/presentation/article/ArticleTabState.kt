package com.devlog.article.presentation.article

import com.devlog.article.data.entity.ArticleEntity

data class ArticleTabState(
    var articles: ArrayList<ArticleEntity> = ArrayList(),
    val keyword : Int,
    val maxPage : Int,
    var page: Int = 1
)