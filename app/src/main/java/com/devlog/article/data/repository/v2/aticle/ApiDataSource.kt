package com.devlog.article.data.repository.v2.aticle

import com.devlog.article.data.entity.article.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ApiDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun postLogin(loginEntity: LoginEntity):ApiResponse<DefaultResponse>{
        return apiService.postLogin(loginEntity)
    }
    suspend fun getArticle(page:Int):ApiResponse<ArticleResponse>{
        return apiService.getArticle(page = page)
    }
    suspend fun getArticleKeyword(articleKeywordRequest: ArticleKeywordRequest): ApiResponse<ArticleResponse> {
        return  apiService.getArticleKeyword(articleKeywordRequest.keyword,articleKeywordRequest.page)
    }

    suspend fun getArticleSeveralKeywordUseCase(keywords:ArrayList<Int>,page: Int):ApiResponse<ArticleSeveralKeywordResponse>{
        return apiService.getArticleSeveralKeyword(keywords,page)
    }

    suspend fun getBookMaker(): ApiResponse<BookmarkResponse>{
        return apiService.getBookMaker()
    }

}