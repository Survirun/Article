package com.devlog.article.data.network

import com.devlog.article.data.entity.article.ArticleLogEntity
import com.devlog.article.data.entity.article.LoginEntity
import com.devlog.article.data.entity.article.MyKeyword
import com.devlog.article.data.entity.article.Passed
import com.devlog.article.data.network.NetworkModule.AddHeaderInterceptor
import com.devlog.article.data.network.Url.PRODUCT_BASE_URL
import com.devlog.article.data.request.ArticleSeveralKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import com.skydoves.sandwich.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    //로그인
    @POST("user/auth")
    suspend fun postLogin(@Body loginEntity: LoginEntity): ApiResponse<DefaultResponse>
    //내 키워드 관심사 수정

    @POST("user/my/keywords")
    suspend fun pathMyKeywords(@Body keywords: MyKeyword): Response<DefaultResponse>

    //유저 정보 가져오기
    @GET("user/my")
    suspend fun getUserInfo(): Response<UserInfoEntity>

    //유저 기사 가져오기
    @GET("v2/article")
    suspend fun getArticle(
        @Query("page") page: Int,
    ): ApiResponse<ArticleResponse>

    //북마크 삭제 또는 추가 서버에서 판단함
    @POST("bookmark/{articleId}")
    suspend fun postBookmark(@Path("articleId") articleId: String): Response<DefaultResponse>

    //북마크 가져오기
    @GET("bookmark")
    suspend fun getBookMaker(): ApiResponse<BookmarkResponse>

    @POST("log/multi")
    suspend fun postArticleLog(@Body logs: ArticleLogEntity): Response<DefaultResponse>

    @POST("article/report/{articleId}")
    suspend fun postReport(@Path("articleId") articleId: String): Response<DefaultResponse>

    @DELETE("user/my")
    suspend fun userDelete(): Response<Any>

    @POST("/v2/article/{keyword}")
    suspend fun getArticleKeyword(
        @Path("keyword") keyword: Int,
        @Query("page") page: Int
    ) : ApiResponse<ArticleResponse>

    //TODO 양현준
    @GET("v2/article/keywords")
    suspend fun getArticleSeveralKeyword(@Query("keywords") keywords:ArrayList<Int>, @Query("page") page: Int):ApiResponse<ArticleSeveralKeywordResponse>


    companion object {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(AddHeaderInterceptor())
//            .addInterceptor(ReceivedHeaderInterceptor())
                .build()
        }
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .client(provideOkHttpClient())
                .baseUrl(PRODUCT_BASE_URL)  // 실제 API의 기본 URL로 변경해야 합니다.
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }


}