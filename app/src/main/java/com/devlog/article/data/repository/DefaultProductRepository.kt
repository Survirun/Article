package com.devlog.article.data.repository



import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.entity.MyKeyword
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.UserInfoEntity

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.ArrayList

class DefaultRepository private constructor(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
): Repository {
    companion object {

        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: DefaultRepository? = null
        fun getInstance( api: ApiService,ioDispatcher: CoroutineDispatcher): DefaultRepository {
            if (instance == null) instance = DefaultRepository(api,ioDispatcher)
            return instance as DefaultRepository
        }
    }
    override suspend fun postLogin(loginEntity: LoginEntity):Int = withContext(ioDispatcher) {
        val response=api.postLogin(loginEntity)
        Log.e("dfsafdsfa",response.code().toString())
        return@withContext if (response.isSuccessful){
            response.code()
        }else{
            500
        }
    }

    override suspend fun pathMyKeywords(keywords: Array<Int>): Boolean = withContext(ioDispatcher){
        val response=api.pathMyKeywords(MyKeyword(keywords))
        Log.e("fdsfasf",response.code().toString())
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
        Log.e("adfasf",response.code().toString())
        return@withContext if (response.isSuccessful){
            response.body()?.toEntity()
        }else{
            null
        }
    }

}