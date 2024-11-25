package com.devlog.article.data.repository.v3

import com.devlog.date.response.ArticleResponse
import com.devlog.date.response.ArticleSeveralKeywordResponse
import com.devlog.date.response.BookmarkResponse
import com.devlog.date.response.DefaultResponse
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
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