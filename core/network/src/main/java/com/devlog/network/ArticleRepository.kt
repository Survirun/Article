package com.devlog.network

import com.devlog.date.response.ArticleResponse
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
import com.skydoves.sandwich.ApiResponse

interface ArticleRepository {

    suspend fun getArticle(page:Int,passed:ArrayList<String>): ApiResponse<ArticleResponse>

    suspend fun postBookmark(articleId:String):Boolean


    suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>):Boolean

    suspend fun postReport(articleId: String): Boolean

    suspend fun getArticleKeyword(articleKeywordRequest : ArticleKeywordRequest) : ApiResponse<ArticleResponse>
}