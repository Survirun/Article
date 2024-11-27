package com.devlog.feature_sign_in.state



sealed class SignInState {
    data object Initialize : SignInState()
    data object SignInSuccess : SignInState()
    data class  SignIException(val errorCode:String) :SignInState()
    data class  SignInError(val errorCode:String) :SignInState()
}