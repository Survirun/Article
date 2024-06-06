package com.devlog.article.presentation.article

import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.presentation.splash.SplashState

sealed class ArticleState {
    object Uninitialized: ArticleState()
    object Loading: ArticleState()


}