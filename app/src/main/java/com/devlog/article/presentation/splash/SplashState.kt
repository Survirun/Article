package com.devlog.article.presentation.splash

import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.Data

sealed  class SplashState {
    object Initialize:SplashState()
    object Loading:SplashState()

    data class GetBookMaker(var bookMakerList:ArrayList<ArticleEntity>)  : SplashState()

    data class GetArticle(var articleResponseMap : Map<String, Data>): SplashState()

    object  GetArticleFail: SplashState()
}