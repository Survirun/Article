package com.devlog.data.repository.v2.aticle

import com.devlog.date.entity.article.LoginEntity
import com.devlog.date.entity.article.Passed
import com.devlog.date.response.ArticleResponse
import com.devlog.date.response.ArticleSeveralKeywordResponse
import com.devlog.date.response.BookmarkResponse
import com.devlog.date.response.DefaultResponse
import com.devlog.model.data.entity.request.ArticleKeywordRequest
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

    suspend fun getArticleSeveralKeyword(keywords:ArrayList<Int>,page: Int):ApiResponse<ArticleSeveralKeywordResponse>{
        return apiDataSource.getArticleSeveralKeywordUseCase(keywords,page)
    }

    suspend fun getBookMaker() : ApiResponse<BookmarkResponse>{
        return apiDataSource.getBookMaker()
    }


}