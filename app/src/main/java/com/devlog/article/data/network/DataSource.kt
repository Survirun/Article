package com.devlog.article.data.network

import com.devlog.article.data.entity.article.LoginEntity
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import com.devlog.model.data.entity.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse

interface DataSource {
    suspend fun postLogin(loginEntity: LoginEntity):ApiResponse<DefaultResponse>
    suspend fun getArticle(page:Int): ApiResponse<ArticleResponse>

    suspend fun postBookmark(articleId:String):ApiResponse<DefaultResponse>


    suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>):ApiResponse<DefaultResponse>

    suspend fun postReport(articleId: String): ApiResponse<DefaultResponse>

    suspend fun getArticleKeyword(articleKeywordRequest : ArticleKeywordRequest) : ApiResponse<ArticleResponse>

    suspend fun pathMyKeywords(keywords: Array<Int>): ApiResponse<DefaultResponse>

    suspend fun getUserInfo(): ApiResponse<UserInfoEntity>
    suspend fun deleteUser(): ApiResponse<DefaultResponse>

    suspend fun getBookMaker() : ApiResponse<BookmarkResponse>

    suspend fun getArticleSeveralKeyword(keywords:ArrayList<Int>,page: Int):ApiResponse<ArticleSeveralKeywordResponse>
}