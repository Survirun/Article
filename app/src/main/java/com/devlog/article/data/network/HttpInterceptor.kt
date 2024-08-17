package com.devlog.article.data.network

import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.ArticleApplication
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Response
@Module
@InstallIn(SingletonComponent::class)
class HttpInterceptor :
    Interceptor {
    val userPreference by lazy {
        UserPreference.getInstance(ArticleApplication.instance)
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("uid", userPreference.userUid)

            .build()
        return chain.proceed(request)
    }
}
