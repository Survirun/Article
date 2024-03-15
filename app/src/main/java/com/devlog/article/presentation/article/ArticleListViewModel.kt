package com.devlog.article.presentation.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.naver.provideNaverRetrofit
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.Repository
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.extensions.toMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleListViewModel : ViewModel() {
    lateinit var succeed: () -> Unit
    lateinit var failed: () -> Unit
    lateinit var reportSucceed: () -> Unit
    lateinit var reportFailed: () -> Unit
    lateinit var article: ArticleResponse
    lateinit var bookmark: ArrayList<ArticleEntity>

    fun getArticle(passed: ArrayList<String>): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle(1, passed)
        if (serverCode != null) {
            article = serverCode
            succeed()

        } else {
            failed()
        }


    }

    fun postArticleLog(postArticleLogResponse: ArrayList<ArticleLogResponse>): Job =
        viewModelScope.launch {
            val api = ApiService(
                provideProductRetrofit(
                    buildOkHttpClient(),
                    provideGsonConverterFactory()
                )
            )
            val repository: Repository =
                DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
            val serverCode = repository.postArticleLog(postArticleLogResponse)
            if (serverCode != null) {

            } else {
            }

        }

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

        } else {
            failed()
        }

    }

    fun getBookMaker(): Job = viewModelScope.launch {
        val api = ApiService(
            provideNaverRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getBookMaker()
        serverCode?.data?.forEach {
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
    }

    fun postReport(articleId: String): Job = viewModelScope.launch {
        val api = ApiService(
            provideNaverRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.postReport(articleId)
        if (serverCode) {
            reportSucceed()
        } else {
            reportFailed()
        }

    }
}