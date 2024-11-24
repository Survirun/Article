package com.devlog.network

import androidx.core.os.trace
import com.devlog.date.response.ArticleResponse
import com.devlog.model.data.entity.article.ArticleLogEntity
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
import com.devlog.network.Url.PRODUCT_BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitNiaNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
): ArticleRepository{

    private val api = trace("ApiService") {
        Retrofit.Builder()
            .baseUrl(PRODUCT_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(ApiService::class.java)
    }


    override suspend fun getArticle(page: Int, passed: ArrayList<String>): ApiResponse<ArticleResponse>  {
        val response = api.getArticle(page)
        return response

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