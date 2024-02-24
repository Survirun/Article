package com.devlog.article.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.naver.provideNaverRetrofit
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.Repository
import com.devlog.article.data.response.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {
    lateinit var succeed: () -> Unit
    lateinit var failed: () -> Unit
    var article = ArrayList<ArticleEntity>()

    fun postBookmark(articleId: String): Job = viewModelScope.launch {
        val api = ApiService(
            provideNaverRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.postBookmark(articleId)
        if (serverCode) {
            succeed()
        } else {
            failed()
        }

    }
}