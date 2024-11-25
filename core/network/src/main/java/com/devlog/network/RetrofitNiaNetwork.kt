package com.devlog.network

import androidx.core.os.trace
import com.devlog.date.entity.article.LoginEntity
import com.devlog.date.entity.article.MyKeyword
import com.devlog.model.data.entity.response.ArticleResponse
import com.devlog.date.response.ArticleSeveralKeywordResponse
import com.devlog.date.response.BookmarkResponse
import com.devlog.date.response.DefaultResponse
import com.devlog.date.response.UserInfoEntity
import com.devlog.model.data.entity.article.ArticleLogEntity
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
import com.devlog.preference.PrefManager
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
): DataSource {
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

    override suspend fun postLogin(loginEntity: LoginEntity): ApiResponse<DefaultResponse> {
        return  api.postLogin(loginEntity= loginEntity)
    }


    override suspend fun getArticle(page: Int): ApiResponse<ArticleResponse> {


        return api.getArticle(page)

    }


    override suspend fun postBookmark(articleId: String):ApiResponse<DefaultResponse>  {
        val response = api.postBookmark(articleId)
        return response
    }



    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): ApiResponse<DefaultResponse> {
        val response = api.postArticleLog(ArticleLogEntity(articleLogResponse))
        return response
    }


    override suspend fun postReport(articleId: String): ApiResponse<DefaultResponse>  {
        val response = api.postReport(articleId)
        return response
    }

    override suspend fun getArticleKeyword(
        articleKeywordRequest : ArticleKeywordRequest
    ): ApiResponse<ArticleResponse>  {
        val response = api.getArticleKeyword(articleKeywordRequest.keyword, articleKeywordRequest.page)
        return response
    }

    override suspend fun pathMyKeywords(keywords: Array<Int>): ApiResponse<DefaultResponse> {
       return api.pathMyKeywords(keywords= MyKeyword(keywords))
    }

    override suspend fun getUserInfo(): ApiResponse<UserInfoEntity> {
        return  api.getUserInfo()
    }

    override suspend fun deleteUser(): ApiResponse<DefaultResponse> {
        return  api.userDelete()
    }

    override suspend fun getBookMaker(): ApiResponse<BookmarkResponse> {
        return  api.getBookMaker()
    }

    override suspend fun getArticleSeveralKeyword(
        keywords: ArrayList<Int>,
        page: Int
    ): ApiResponse<ArticleSeveralKeywordResponse> {
        return  api.getArticleSeveralKeyword(keywords,page)
    }


}