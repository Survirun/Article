package com.devlog.article.data.repository.v3

import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.skydoves.sandwich.ApiResponse

interface ArticleRepository3 {

    suspend fun getArticle(page:Int): ApiResponse<ArticleResponse>

    suspend fun postBookmark(articleId:String):Boolean


    suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>):Boolean

    suspend fun postReport(articleId: String): Boolean

    suspend fun getArticleKeyword(articleKeywordRequest : ArticleKeywordRequest) : ApiResponse<ArticleResponse>
}