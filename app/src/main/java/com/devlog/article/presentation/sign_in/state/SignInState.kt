package com.devlog.article.presentation.sign_in.state

import com.devlog.article.presentation.main.state.MainApiState

sealed class SignInState {
    data object Initialize : SignInState()
    data object SignInSuccess : SignInState()
    data class  SignIException(val errorCode:String) :SignInState()
    data class  SignInError(val errorCode:String) :SignInState()
}