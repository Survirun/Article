package com.devlog.article.data.network.naver

import com.devlog.article.data.entity.naver.ApiData
import com.devlog.article.data.entity.naver.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NaverApi {

    @POST("text-summary/v1/summarize")
    suspend fun postTextSummary(@Body apiData: ApiData): Response<ApiResponse>

}