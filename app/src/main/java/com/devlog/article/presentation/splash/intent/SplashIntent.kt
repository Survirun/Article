package com.devlog.article.presentation.splash.intent

sealed  class SplashIntent {


    object GetArticle : SplashIntent()
    object GetArticleKeywordList :  SplashIntent()
    object GetBookMaker : SplashIntent()


}