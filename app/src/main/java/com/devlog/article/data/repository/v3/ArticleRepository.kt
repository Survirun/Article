package com.devlog.article.data.repository.v3

import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.devlog.model.data.entity.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse

interface ArticleRepository {

    suspend fun getArticle(page:Int): ApiResponse<ArticleResponse>

    suspend fun postBookmark(articleId:String): ApiResponse<DefaultResponse>


    suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): ApiResponse<DefaultResponse>

    suspend fun postReport(articleId: String): ApiResponse<DefaultResponse>

    suspend fun getArticleKeyword(articleKeywordRequest : ArticleKeywordRequest) : ApiResponse<ArticleResponse>
    suspend fun getBookMaker() : ApiResponse<BookmarkResponse>
    suspend fun getArticleSeveralKeyword(keywords:ArrayList<Int>,page: Int):ApiResponse<ArticleSeveralKeywordResponse>

}