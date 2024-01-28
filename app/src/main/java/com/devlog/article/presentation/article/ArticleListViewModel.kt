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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleListViewModel : ViewModel() {
    lateinit var succeed: () -> Unit
    lateinit var failed: () -> Unit

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
        Log.e("afdafsd",serverCode!!.data.toString())
//        if (serverCode) {
//            succeed()
//        } else {
//            failed()
//        }


    }
}