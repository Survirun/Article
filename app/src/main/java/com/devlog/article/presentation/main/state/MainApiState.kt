package com.devlog.article.presentation.main.state



sealed class MainApiState {
    data object Initialize : MainApiState()
    data object SignInSuccess : MainApiState()
    data class  SignIException(val errorCode:String) : MainApiState()
    data class  SignInError(val errorCode:String) : MainApiState()
}