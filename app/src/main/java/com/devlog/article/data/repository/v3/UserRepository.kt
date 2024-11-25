package com.devlog.article.data.repository.v3

import com.devlog.article.data.entity.article.LoginEntity
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import com.skydoves.sandwich.ApiResponse

interface UserRepository {
    suspend fun postLogin(loginEntity: LoginEntity):ApiResponse<DefaultResponse>

    suspend fun pathMyKeywords(keywords: Array<Int>): ApiResponse<DefaultResponse>

    suspend fun getUserInfo(): ApiResponse<UserInfoEntity>
    suspend fun deleteUser(): ApiResponse<DefaultResponse>
}