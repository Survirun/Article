package com.devlog.feature_splash.state

sealed class SplashUiState {
    object Loding : SplashUiState()
    object Success :  SplashUiState()
}