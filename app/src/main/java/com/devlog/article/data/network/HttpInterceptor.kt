package com.devlog.article.data.network


import com.devlog.article.data.preference.PrefManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Response
@Module
@InstallIn(SingletonComponent::class)
class HttpInterceptor :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("uid", PrefManager.userUid)
            .addHeader("guest-mode","true")
            .build()
        return chain.proceed(request)
    }
}
