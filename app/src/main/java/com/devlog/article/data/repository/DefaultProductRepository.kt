package com.devlog.article.data.repository



import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.UserInfoEntity

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRepository(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
): Repository {
    override suspend fun postLogin(loginEntity: LoginEntity):Int = withContext(ioDispatcher) {
        val response=api.postLogin(loginEntity)
        return@withContext if (response.isSuccessful){
            response.code()
        }else{
            500
        }
    }

    override suspend fun pathMyKeywords(keywords: Array<String>): Boolean = withContext(ioDispatcher){
        val response=api.pathMyKeywords(keywords)
        return@withContext response.isSuccessful
    }

    override suspend fun getUserInfo(): UserInfoEntity? = withContext(ioDispatcher) {
        val response=api.getUserInfo()
        return@withContext if (response.isSuccessful){
             response.body()?.toEntity()
        }else{
            null
        }
    }

    override suspend fun getArticle(): ArticleResponse? = withContext(ioDispatcher) {
        val response=api.getArticle()
        return@withContext if (response.isSuccessful){
            response.body()?.toEntity()
        }else{
            null
        }
    }

}