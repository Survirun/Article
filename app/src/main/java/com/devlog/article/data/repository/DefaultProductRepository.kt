package com.devlog.article.data.repository



import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.devlog.article.data.entity.ArticleLogEntity
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.entity.MyKeyword
import com.devlog.article.data.entity.Page
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.ArticleLogResponse
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
        return@withContext if (response.isSuccessful){
            response.code()
        }else{
            500
        }
    }

    override suspend fun pathMyKeywords(keywords: Array<Int>): Boolean = withContext(ioDispatcher){
        val response=api.pathMyKeywords(MyKeyword(keywords))
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
        val response=api.getArticle(1)
        return@withContext if (response.isSuccessful){
            response.body()
        }else{
            null
        }
    }

    override suspend fun postBookmark(articleId:String): Boolean = withContext(ioDispatcher) {
        val response=api.postBookmark(articleId)
        Log.e("test",response.code().toString())
        return@withContext response.isSuccessful
    }

    override suspend fun getBookMaker(): ArticleResponse? = withContext(ioDispatcher){
        val response=api.getBookMaker()
        return@withContext  response.body()
    }

    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): Boolean= withContext(ioDispatcher) {
        Log.e("polaris","실행2")
        val response=api.postArticleLog(ArticleLogEntity(articleLogResponse))
        Log.e("polaris",response.message().toString())
        Log.e("polaris",response.body().toString())
        return@withContext response.isSuccessful
    }

}