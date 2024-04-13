package com.devlog.article.presentation.splash

import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.response.ArticleResponse

sealed  class SplashState {
    object Uninitialized:SplashState()
    object Loading:SplashState()

    data class GetBookMaker(var bookMakerList:ArrayList<ArticleEntity>)  : SplashState()

    data class GetArticle(var articleResponse : ArticleResponse,var category: Int): SplashState()
}