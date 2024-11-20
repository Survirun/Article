package com.devlog.article.presentation.sign_in.inetnt

import com.devlog.article.presentation.main.intent.MainIntent

sealed class SignInIntent {
    data class PostLogin(val uid:String,val email:String,val name:String): SignInIntent()
}