package com.devlog.article.data.network

import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.entity.MyKeyword
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.LoginResponse
import com.devlog.article.data.response.UserInfoEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.ArrayList

interface ApiService {
    //로그인
    @POST("user/auth")
    suspend fun postLogin(@Body loginEntity: LoginEntity):Response<LoginResponse>
    //내 키워드 관심사 수정

    @POST("user/my/keywords")
    suspend fun pathMyKeywords(@Body keywords: MyKeyword):Response<LoginResponse>

    //유저 정보 가져오기
    @GET("user/my")
    fun getUserInfo():Response<UserInfoEntity>

    @GET("article")
    fun getArticle():Response<ArticleResponse>
}