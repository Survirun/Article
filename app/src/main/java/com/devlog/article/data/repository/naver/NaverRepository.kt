package com.devlog.article.data.repository.naver

import android.annotation.SuppressLint
import android.util.Log
import com.devlog.article.data.network.naver.NaverApi
import com.devlog.article.data.entity.naver.ApiData

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NaverRepository private constructor(
    private val api: NaverApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    companion object {

        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: NaverRepository? = null
        fun getInstance(api: NaverApi, ioDispatcher: CoroutineDispatcher): NaverRepository {
            if (instance == null) instance = NaverRepository(api, ioDispatcher)
            return instance as NaverRepository
        }
    }

    suspend fun postAiSummary(apiData: ApiData): String = withContext(ioDispatcher) {
        var response = api.postTextSummary(apiData)

        if (response.isSuccessful) {
            return@withContext response.body()!!.summary
        } else {
            return@withContext ""
        }

    }

}