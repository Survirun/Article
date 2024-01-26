package com.devlog.article.data.network

import android.app.Application
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.ArticleApplication
import com.google.firebase.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

internal fun ApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}
//fun test(){
//    var s= ApiService(provideProductRetrofit(buildOkHttpClient(),provideGsonConverterFactory()))
//
//}
internal fun provideProductRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.PRODUCT_BASE_URL)
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
//로그인 할 때 만 사용
internal fun LoginBuildOkHttpClient(): OkHttpClient {
    val interceptor= HttpLoggingInterceptor()
    if(BuildConfig.DEBUG){
        interceptor.level= HttpLoggingInterceptor.Level.BODY

    }else{
        interceptor.level= HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}

internal fun buildOkHttpClient(): OkHttpClient {
    val interceptor= HttpLoggingInterceptor()
    if(BuildConfig.DEBUG){
        interceptor.level= HttpLoggingInterceptor.Level.BODY

    }else{
        interceptor.level= HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .addInterceptor(HttpInterceptor())
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}

