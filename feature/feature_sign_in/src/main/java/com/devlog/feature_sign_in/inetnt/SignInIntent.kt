package com.devlog.feature_sign_in.inetnt

sealed class SignInIntent {
    data class PostLogin(val uid:String,val email:String,val name:String): SignInIntent()
}