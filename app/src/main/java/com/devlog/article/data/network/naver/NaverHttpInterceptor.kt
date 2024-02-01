package com.devlog.article.data.network.naver

import okhttp3.Interceptor
import okhttp3.Response


class NaverHttpInterceptor :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-NCP-APIGW-API-KEY-ID", "iicy4ipu6z")
            .addHeader("X-NCP-APIGW-API-KEY","wLvSPyMwoMb75Tl3Il8UZbEEByjq47ekrCM5mcLE")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}
