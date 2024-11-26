package com.devlog.feature_article_list.intent

sealed class ArticleIntent {
    data class getArticle(val page:Int): ArticleIntent()
    data class  getArticleKeyword(val page:Int,val keyword:Int): ArticleIntent()
    object postDeleteAccount: ArticleIntent()

}