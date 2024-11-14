package com.devlog.article.presentation.splash.state


import com.devlog.article.data.response.Article
import com.devlog.article.data.response.Data

sealed class SplashApiState() {

    object Loading : SplashApiState()

    // 각각의 API에 맞는 성공 상태 정의
    data class GetArticleSuccess(var articleResponseMap : Map<String, Data>) : SplashApiState()
    data class GetArticleKeywordsSuccess(var articleResponseMap : Map<String, Data>) : SplashApiState()
    data class GetBookMakerSuccess(var bookMakerList:List<Article>) : SplashApiState()

    // 각각의 API에 맞는 실패 상태 정의
    data class Failure(val error: String) : SplashApiState()
}