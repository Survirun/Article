package com.devlog.network

import com.devlog.date.entity.article.LoginEntity
import com.devlog.date.entity.article.MyKeyword
import com.devlog.date.response.ArticleResponse
import com.devlog.date.response.ArticleSeveralKeywordResponse
import com.devlog.date.response.BookmarkResponse
import com.devlog.date.response.DefaultResponse
import com.devlog.date.response.UserInfoEntity
import com.devlog.model.data.entity.article.ArticleLogEntity
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //로그인
    @POST("user/auth")
    suspend fun postLogin(@Body loginEntity: LoginEntity): ApiResponse<DefaultResponse>
    //내 키워드 관심사 수정

    @POST("user/my/keywords")
    suspend fun pathMyKeywords(@Body keywords: MyKeyword): ApiResponse<DefaultResponse>

    //유저 정보 가져오기
    @GET("user/my")
    suspend fun getUserInfo(): ApiResponse<UserInfoEntity>

    //유저 기사 가져오기
    @GET("v2/article")
    suspend fun getArticle(
        @Query("page") page: Int,
    ): ApiResponse<ArticleResponse>

    //북마크 삭제 또는 추가 서버에서 판단함
    @POST("bookmark/{articleId}")
    suspend fun postBookmark(@Path("articleId") articleId: String): ApiResponse<DefaultResponse>

    //북마크 가져오기
    @GET("bookmark")
    suspend fun getBookMaker(): ApiResponse<BookmarkResponse>

    @POST("log/multi")
    suspend fun postArticleLog(@Body logs: ArticleLogEntity): ApiResponse<DefaultResponse>

    @POST("article/report/{articleId}")
    suspend fun postReport(@Path("articleId") articleId: String): ApiResponse<DefaultResponse>

    @DELETE("user/my")
    suspend fun userDelete(): ApiResponse<DefaultResponse>

    @POST("/v2/article/{keyword}")
    suspend fun getArticleKeyword(
        @Path("keyword") keyword: Int,
        @Query("page") page: Int
    ) : ApiResponse<ArticleResponse>

    //TODO 양현준
    @GET("v2/article/keywords")
    suspend fun getArticleSeveralKeyword(@Query("keywords") keywords:ArrayList<Int>, @Query("page") page: Int):ApiResponse<ArticleSeveralKeywordResponse>




}