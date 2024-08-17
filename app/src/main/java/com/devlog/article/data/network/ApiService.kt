package com.devlog.article.data.network

import com.devlog.article.data.entity.ArticleLogEntity
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.entity.MyKeyword
import com.devlog.article.data.entity.Passed
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import com.skydoves.sandwich.ApiResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //로그인
    @POST("user/auth")
    suspend fun postLogin(@Body loginEntity: LoginEntity): Response<DefaultResponse>
    //내 키워드 관심사 수정

    @POST("user/my/keywords")
    suspend fun pathMyKeywords(@Body keywords: MyKeyword): Response<DefaultResponse>

    //유저 정보 가져오기
    @GET("user/my")
    suspend fun getUserInfo(): Response<UserInfoEntity>

    //유저 기사 가져오기
    @POST("article")
    suspend fun getArticle(
        @Query("page") page: Int,
        @Body passed: Passed
    ): Response<ArticleResponse>

    //북마크 삭제 또는 추가 서버에서 판단함
    @POST("bookmark/{articleId}")
    suspend fun postBookmark(@Path("articleId") articleId: String): Response<DefaultResponse>

    //북마크 가져오기
    @GET("bookmark")
    suspend fun getBookMaker(): Response<BookmarkResponse>

    @POST("log/multi")
    suspend fun postArticleLog(@Body logs: ArticleLogEntity): Response<DefaultResponse>

    @POST("article/report/{articleId}")
    suspend fun postReport(@Path("articleId") articleId: String): Response<DefaultResponse>

    @DELETE("user/my")
    suspend fun userDelete(): Response<Any>

    @POST("article/{keyword}")
    suspend fun getArticleKeyword(
        @Path("keyword") keyword: Int,
        @Query("page") page: Int,
        @Body passed: Passed
    ) : ApiResponse<ArticleResponse>
}