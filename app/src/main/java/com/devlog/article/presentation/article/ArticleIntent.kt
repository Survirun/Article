package com.devlog.article.presentation.article

import com.devlog.article.data.entity.ArticleEntity

sealed  class ArticleIntent  {
    data class LoadArticles(val keyword: Int, val page: Int, val pass: String) : ArticleIntent()
}



