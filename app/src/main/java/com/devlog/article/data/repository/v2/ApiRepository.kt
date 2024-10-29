package com.devlog.article.data.repository.v2

import com.devlog.article.data.entity.article.LoginEntity
import com.devlog.article.data.entity.article.Passed
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.BookmarkResponse
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

    suspend fun getArticleSeveralKeyword(keywords:List<Int>,page: Int):ApiResponse<ArticleSeveralKeywordResponse>{
        return apiDataSource.getArticleSeveralKeywordUseCase(keywords,page)
    }

    suspend fun getBookMaker() : ApiResponse<BookmarkResponse>{
        return apiDataSource.getBookMaker()
    }


}