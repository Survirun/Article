package com.devlog.article.presentation.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.Repository
import com.devlog.article.data.response.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleListViewModel : ViewModel() {
    lateinit var succeed: () -> Unit
    lateinit var failed: () -> Unit
    var article= listOf<Article>()
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
        Log.e("asdfaadfadfa",serverCode!!.data[0].toString())
        article=serverCode.data
        if (serverCode!=null) {
            succeed()
        } else {
            failed()
        }


    }
}