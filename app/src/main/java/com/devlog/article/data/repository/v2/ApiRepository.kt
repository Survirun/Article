package com.devlog.article.data.repository.v2

import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.entity.Passed
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.DefaultResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject


class ApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {

    suspend fun postLogin(loginEntity: LoginEntity):ApiResponse<DefaultResponse>{
        return apiDataSource.postLogin(loginEntity =loginEntity )
    }
    suspend fun getArticle(page:Int,passed: Passed):ApiResponse<ArticleResponse>{
        return apiDataSource.getArticle(page,passed)
    }

    suspend fun getArticleKeyword(articleKeywordRequest: ArticleKeywordRequest): ApiResponse<ArticleResponse> {
        return  apiDataSource.getArticleKeyword(articleKeywordRequest)
    }

    suspend fun getArticleSeveralKeywordUseCase(keywords:ArrayList<Int>,page: Int):ApiResponse<ArticleSeveralKeywordResponse>{
        return apiDataSource.getArticleSeveralKeywordUseCase(keywords,page)
    }


}