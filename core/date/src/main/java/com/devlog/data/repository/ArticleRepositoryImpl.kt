package com.devlog.article.data.repository

import android.annotation.SuppressLint
import com.devlog.data.repository.ArticleRepository
import com.devlog.date.response.ArticleResponse
import com.devlog.model.data.entity.article.ArticleLogEntity
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
import com.devlog.network.ApiService
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ArticleRepositoryImpl private constructor(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) : ArticleRepository {
    companion object {
        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: ArticleRepositoryImpl? = null
        fun getInstance(api: ApiService, ioDispatcher: CoroutineDispatcher): ArticleRepositoryImpl {
            if (instance == null) instance = ArticleRepositoryImpl(api, ioDispatcher)
            return instance as ArticleRepositoryImpl
        }
    }


    override suspend fun getArticle(page: Int, passed: ArrayList<String>): ApiResponse<ArticleResponse>  =
        withContext(ioDispatcher) {
            val response = api.getArticle(page)
            return@withContext response
        }

    override suspend fun postBookmark(articleId: String): Boolean = withContext(ioDispatcher) {
        val response = api.postBookmark(articleId)
        return@withContext response.isSuccessful
    }



    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): Boolean =
        withContext(ioDispatcher) {
            val response = api.postArticleLog(ArticleLogEntity(articleLogResponse))
            return@withContext response.isSuccessful
        }

    override suspend fun postReport(articleId: String): Boolean = withContext(ioDispatcher) {
        val response = api.postReport(articleId)
        return@withContext response.isSuccessful
    }

    override suspend fun getArticleKeyword(
        articleKeywordRequest : ArticleKeywordRequest
    ): ApiResponse<ArticleResponse> = withContext(ioDispatcher) {
        val response = api.getArticleKeyword(articleKeywordRequest.keyword, articleKeywordRequest.page)
        return@withContext response
    }

}