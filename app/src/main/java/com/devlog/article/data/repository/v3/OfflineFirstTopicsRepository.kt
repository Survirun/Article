package com.devlog.article.data.repository.v3

import com.devlog.article.data.network2.ArticleDataSource
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

internal class OfflineFirstTopicsRepository @Inject constructor(
    private val network: ArticleDataSource
) : ArticleRepository3 {
    override suspend fun getArticle(
        page: Int
    ): ApiResponse<ArticleResponse> {
        return  network.getArticle(page)
    }

    override suspend fun postBookmark(articleId: String): Boolean {
        return  network.postBookmark(articleId)
    }

    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): Boolean {
        return  network.postBookmark(articleLogResponse.toString())
    }

    override suspend fun postReport(articleId: String): Boolean {
        return  network.postReport(articleId)
    }

    override suspend fun getArticleKeyword(articleKeywordRequest: ArticleKeywordRequest): ApiResponse<ArticleResponse> {
        return  network.getArticleKeyword(articleKeywordRequest)
    }


}
