package com.devlog.article.presentation.article.intent

sealed class ArticleIntent {
    data class getArticle(val page:Int): ArticleIntent()
    data class  getArticleKeyword(val page:Int,val keyword:Int): ArticleIntent()
    object postDeleteAccount: ArticleIntent()

}