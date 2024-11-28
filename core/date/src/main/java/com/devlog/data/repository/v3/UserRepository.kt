package com.devlog.data.repository.v3

import com.devlog.date.entity.article.LoginEntity
import com.devlog.date.response.DefaultResponse
import com.devlog.date.response.UserInfoEntity
import com.skydoves.sandwich.ApiResponse

interface UserRepository {
    suspend fun postLogin(loginEntity: LoginEntity):ApiResponse<DefaultResponse>

    suspend fun pathMyKeywords(keywords: Array<Int>): ApiResponse<DefaultResponse>

    suspend fun getUserInfo(): ApiResponse<UserInfoEntity>
    suspend fun deleteUser(): ApiResponse<DefaultResponse>
}