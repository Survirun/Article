package com.devlog.article.presentation.article.state

sealed class ArticleState {
    object Uninitialized: ArticleState()
    object Loading: ArticleState()


}