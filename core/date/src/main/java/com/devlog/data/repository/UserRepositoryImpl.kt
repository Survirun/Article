package com.devlog.article.data.repository


import android.annotation.SuppressLint
import com.devlog.date.entity.article.MyKeyword
import com.devlog.date.response.UserInfoEntity
import com.devlog.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRepository private constructor(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    companion object {

        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: DefaultRepository? = null
        fun getInstance(api: ApiService, ioDispatcher: CoroutineDispatcher): DefaultRepository {
            if (instance == null) instance = DefaultRepository(api, ioDispatcher)
            return instance as DefaultRepository
        }
    }



    override suspend fun pathMyKeywords(keywords: Array<Int>): Boolean = withContext(ioDispatcher) {
        val response = api.pathMyKeywords(MyKeyword(keywords))
        return@withContext response.isSuccessful
    }

    override suspend fun getUserInfo(): UserInfoEntity? = withContext(ioDispatcher) {
        val response = api.getUserInfo()
        return@withContext if (response.isSuccessful) {
            response.body()?.toEntity()
        } else {
            null
        }
    }

    override suspend fun deleteUser(): Int = withContext(ioDispatcher) {
        val response = api.userDelete()
        return@withContext response.code()
    }


}