package com.devlog.article.presentation.main.state

import com.devlog.article.presentation.sign_in.state.SignInState

sealed class MainApiState {
    data object Initialize : MainApiState()
    data object SignInSuccess : MainApiState()
    data class  SignIException(val errorCode:String) : MainApiState()
    data class  SignInError(val errorCode:String) : MainApiState()
}