package com.devlog.article.data.network.naver

import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.HttpInterceptor
import com.devlog.article.data.network.Url
import com.google.firebase.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


internal fun NaverService(retrofit: Retrofit): NaverApi {
    return retrofit.create(NaverApi::class.java)
}


internal fun provideNaverRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.NAVER_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

internal fun provideGsonConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create(
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    )
}


internal fun naverBuildOkHttpClient(): OkHttpClient {
    val interceptor= HttpLoggingInterceptor()
    if(BuildConfig.DEBUG){
        interceptor.level= HttpLoggingInterceptor.Level.BODY

    }else{
        interceptor.level= HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder().apply {
        addInterceptor(NaverHttpInterceptor())
        connectTimeout(10, TimeUnit.SECONDS)
        addInterceptor(interceptor)

    }.build()

}