package com.devlog.article.data.repository.v2

import com.devlog.article.data.entity.Passed
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject


class ApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {
    suspend fun getArticle(page:Int,passed: Passed):ApiResponse<ArticleResponse>{
        return apiDataSource.getArticle(page,passed)
    }

    suspend fun getArticleKeyword(articleKeywordRequest: ArticleKeywordRequest): ApiResponse<ArticleResponse> {
        return  apiDataSource.getArticleKeyword(articleKeywordRequest)
    }


}