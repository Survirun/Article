package com.devlog.article.data.network

import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.ArticleApplication
import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor :
    Interceptor {
    val userPreference by lazy {
        UserPreference.getInstance(ArticleApplication.instance)
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("uuid", userPreference.userUid)

            .build()
        return chain.proceed(request)
    }
}