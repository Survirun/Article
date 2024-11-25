package com.devlog.article.data.network2

import androidx.core.os.trace
import com.devlog.article.data.entity.article.ArticleLogEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.Url
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
): ArticleDataSource{
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 응답 본문까지 로깅
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("uid", PrefManager.userUid)
                .addHeader("guest-mode","true")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .build()

    private val api = trace("ApiService") {
        Retrofit.Builder()
            .client(client)
            .baseUrl(Url.PRODUCT_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
           // .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


    override suspend fun getArticle(page: Int): ApiResponse<ArticleResponse> {


        return api.getArticle(page)

    }


    override suspend fun postBookmark(articleId: String): Boolean  {
        val response = api.postBookmark(articleId)
        return response.isSuccessful
    }



    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): Boolean {
        val response = api.postArticleLog(ArticleLogEntity(articleLogResponse))
        return response.isSuccessful
    }


    override suspend fun postReport(articleId: String): Boolean  {
        val response = api.postReport(articleId)
        return response.isSuccessful
    }

    override suspend fun getArticleKeyword(
        articleKeywordRequest : ArticleKeywordRequest
    ): ApiResponse<ArticleResponse>  {
        val response = api.getArticleKeyword(articleKeywordRequest.keyword, articleKeywordRequest.page)
        return response
    }





}