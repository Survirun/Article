package com.devlog.article.data.network

import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.LoginResponse
import com.devlog.article.data.response.UserInfoEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    //로그인
    @POST("/user/auth")
    fun postLogin(@Body loginEntity: LoginEntity):Response<LoginResponse>
    //내 키워드 관심사 수정
    @PATCH("/user/my/keywords")
    fun pathMyKeywords(@Body keywords:Array<String>):Response<LoginResponse>

    //유저 정보 가져오기
    @GET("/user/my")
    fun getUserInfo():Response<UserInfoEntity>

    @GET("/article")
    fun getArticle():Response<ArticleResponse>
}