package com.devlog.article.presentation.main.intent

sealed class MainIntent {
    data class PostSignIn(val uid:String,val email:String,val name:String):MainIntent()

}