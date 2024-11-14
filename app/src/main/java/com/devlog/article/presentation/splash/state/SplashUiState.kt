package com.devlog.article.presentation.splash.state

sealed class SplashUiState {
    object Loding : SplashUiState()
    object Success :  SplashUiState()
}