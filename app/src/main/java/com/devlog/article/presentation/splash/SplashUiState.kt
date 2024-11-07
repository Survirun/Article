package com.devlog.article.presentation.splash

sealed class SplashUiState {
    object Loding : SplashUiState()
    object Success :  SplashUiState()
}