package com.devlog.article.presentation.article

sealed class ArticleState {
    object Uninitialized: ArticleState()
    object Loading: ArticleState()


}