package com.devlog.article.data.network

import android.app.Application
import android.util.Log
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.data.repository.v2.ApiDataSource
import com.devlog.article.data.repository.v2.ApiRepository
import com.devlog.article.presentation.ArticleApplication
import com.google.firebase.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
            level = if (BuildConfig.DEBUG ) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    class AddHeaderInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.d("polaris", PrefManager.userUid)
            val builder = chain.request().newBuilder()
                .addHeader("uid", PrefManager.userUid)
                .addHeader("guest-mode","true")
                //.addHeader("uuid", CredentialPreference.getInstance().uuid)
//                .addHeader("uuid", CredentialPreference.getInstance().uuid)
//                .addHeader("User-Agent", System.getProperty("http.agent").toString())
//                .addHeader("content-language", Locale.getDefault().toLanguageTag())
//                .addHeader("app-version-code", BuildConfig.VERSION_CODE.toString())
//                .addHeader("client-date", PlayTimeParser.getSystemTimeLocalTimeZone())
//                .apply {
//                    val userProfile = UserUtil.userProfile
//                    if (userProfile.jwtToken.isNotEmpty()) {
//                        addHeader("Authorization", userProfile.jwtToken)
//                        Log.e("okhttp", "userProfile.jwtToken : ${userProfile.jwtToken}")
//                    }else if (PrefManager.jwtToken.isNotEmpty()){
//                        addHeader("Authorization", PrefManager.jwtToken)
//                        Log.e("okhttp", "userProfile.jwtToken : ${PrefManager.jwtToken}")
//                    }
//                }

            return try {
                chain.proceed(builder.build())
            } catch (e:Exception){
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(503) // Service Unavailable
                    .message("Service Unavailable")
                    .body(ResponseBody.create(null, e.message ?: "exception"))
                    .build()
            }
        }

    }

    class ReceivedHeaderInterceptor: Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            val headers = originalResponse.headers
            if (headers["sessionValidYn"] == "Y") {

            }

            return originalResponse
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .addInterceptor(AddHeaderInterceptor())
//            .addInterceptor(ReceivedHeaderInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Url.PRODUCT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiDataSource(apiService: ApiService): ApiDataSource {
        return ApiDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideApiRepository(apiDataSource: ApiDataSource): ApiRepository {
        return ApiRepository(apiDataSource)
    }

}

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
        GsonBuilder().create()
    )
}

//로그인 할 때 만 사용
internal fun LoginBuildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY

    } else {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}

internal fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY

    } else {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder().apply {
        addInterceptor(HttpInterceptor())
        connectTimeout(40, TimeUnit.SECONDS)
        addInterceptor(interceptor)

    }.build()


}

