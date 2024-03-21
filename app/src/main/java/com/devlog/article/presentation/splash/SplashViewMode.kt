package com.devlog.article.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.ArticleRepository
import com.devlog.article.data.repository.ArticleRepositoryImpl
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
    var bookmark = ArrayList<ArticleEntity>()
    fun getArticle(page:ArrayList<String>): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: ArticleRepository = ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle(1,page)

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
        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getBookMaker()

        if (serverCode != null) {
            serverCode.data.forEach{
                bookmark.add(
                    ArticleEntity(
                        title = it.title,
                        text = it.snippet!!,
                        image = it.thumbnail!!,
                        url = it.link,
                        articleId = it._id
                    )
                )
            }
            bookmark_succeed()
        }
    }
}