package com.devlog.data.repository.v2.aticle


import com.devlog.date.entity.article.LoginEntity
import com.devlog.date.entity.article.Passed
import com.devlog.date.response.ArticleResponse
import com.devlog.date.response.ArticleSeveralKeywordResponse
import com.devlog.date.response.BookmarkResponse
import com.devlog.date.response.DefaultResponse
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.network.ApiService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ApiDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun postLogin(loginEntity: LoginEntity):ApiResponse<DefaultResponse>{
        return apiService.postLogin(loginEntity)
    }
    suspend fun getArticle(page:Int,passed: Passed):ApiResponse<ArticleResponse>{
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