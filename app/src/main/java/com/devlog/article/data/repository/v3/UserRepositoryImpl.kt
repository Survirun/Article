package com.devlog.article.data.repository.v3

import com.devlog.article.data.entity.article.LoginEntity
import com.devlog.article.data.network.DataSource
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val network: DataSource
) : UserRepository {
    override suspend fun postLogin(loginEntity: LoginEntity): ApiResponse<DefaultResponse> {
       return network.postLogin(loginEntity)
    }


    override suspend fun pathMyKeywords(keywords: Array<Int>): ApiResponse<DefaultResponse> {

        val response = network.pathMyKeywords(keywords)
        return response
    }

    override suspend fun getUserInfo(): ApiResponse<UserInfoEntity> {
        val response = network.getUserInfo()
        return response
    }

    override suspend fun deleteUser(): ApiResponse<DefaultResponse> {
        val response = network.deleteUser()
        return response
    }
}