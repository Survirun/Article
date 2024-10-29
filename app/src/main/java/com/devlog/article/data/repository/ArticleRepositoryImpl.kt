package com.devlog.article.data.repository

import android.annotation.SuppressLint
import com.devlog.article.data.entity.article.ArticleLogEntity
import com.devlog.article.data.entity.article.Passed

import com.devlog.article.data.network.ApiService
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.ArrayList

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
            val response = api.getArticle(page, Passed(passed))
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