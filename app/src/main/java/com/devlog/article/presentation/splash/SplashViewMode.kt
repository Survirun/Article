package com.devlog.article.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.naver.provideNaverRetrofit
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.Repository
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashViewMode():ViewModel() {
    lateinit var succeed: () -> Unit
    lateinit var failed: () -> Unit
    lateinit var bookmark_succeed: () -> Unit
    lateinit var bookmark_failed: () -> Unit
    lateinit var article: ArticleResponse
    var bookmark = listOf<Article>()
    fun getArticle(): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle()
        if (serverCode!=null) {
            article=serverCode
            succeed()

        } else {
            failed()
        }


    }

    fun getBookMaker() : Job=viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getBookMaker()

        if (serverCode!=null) {
            bookmark=serverCode.data
            bookmark_succeed()
        }else bookmark_failed()
    }
}