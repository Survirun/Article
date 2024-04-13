package com.devlog.article.presentation.article_webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.naver.ApiData
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient

import com.devlog.article.data.network.naver.NaverService
import com.devlog.article.data.network.naver.naverBuildOkHttpClient
import com.devlog.article.data.network.naver.provideNaverRetrofit
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.naver.NaverRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleWebViewModel:ViewModel() {
    lateinit var succeed: (text:String) -> Unit
    lateinit var failed: () -> Unit

    fun textSummary(apiData: ApiData):Job=viewModelScope.launch{
        val api = NaverService(
            provideNaverRetrofit(
                naverBuildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        NaverRepository.getInstance(api, ioDispatcher = Dispatchers.IO).postAiSummary(apiData)
        succeed(NaverRepository.getInstance(api, ioDispatcher = Dispatchers.IO).postAiSummary(apiData))
    }
}