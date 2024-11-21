package com.devlog.network

import android.util.Log


import com.devlog.preference.PrefManager
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
            HttpLoggingInterceptor.Level.BODY
            //TODO 나중에 확인해보기
//            level = if (BuildConfig.DEBUG ) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.BODY
//            }
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

//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit): ApiService {
//        return retrofit.create(ApiService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideApiDataSource(apiService: ApiService): ApiDataSource {
//        return ApiDataSource(apiService)
//    }
//
//    @Provides
//    @Singleton
//    fun provideApiRepository(apiDataSource: ApiDataSource): ApiRepository {
//        return ApiRepository(apiDataSource)
//    }

}

internal fun ApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}


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



internal fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
//    if (BuildConfig.DEBUG) {
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//
//    } else {
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//    }
    return OkHttpClient.Builder().apply {
        addInterceptor(HttpInterceptor())
        connectTimeout(40, TimeUnit.SECONDS)
        addInterceptor(interceptor)

    }.build()


}

