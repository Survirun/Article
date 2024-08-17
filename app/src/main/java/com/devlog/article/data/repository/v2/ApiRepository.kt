package com.devlog.article.data.repository.v2

import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject


class ApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {

    suspend fun getArticleKeyword(articleKeywordRequest: ArticleKeywordRequest): ApiResponse<ArticleResponse> {
        return  apiDataSource.getArticleKeyword(articleKeywordRequest)
    }


}