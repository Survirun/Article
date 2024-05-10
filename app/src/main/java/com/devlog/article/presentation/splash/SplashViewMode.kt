package com.devlog.article.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class SplashViewMode() : ViewModel() {
    lateinit var failed: () -> Unit
    lateinit var keyword_failed: () -> Unit
    lateinit var article: ArticleResponse
    val bookmark = ArrayList<ArticleEntity>()

    private var _profileSplashStateLiveData = MutableLiveData<SplashState>(SplashState.Uninitialized)
    val profileSplashStateLiveData: LiveData<SplashState> = _profileSplashStateLiveData

    fun fetchData()= viewModelScope.launch {
        setState(SplashState.Loading)
    }
    fun getArticle(page: ArrayList<String>): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: ArticleRepository = ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle(1, page)

        if (serverCode != null) {
            article = serverCode


        } else {
            failed()
        }


    }

    fun getBookMaker(): Job = viewModelScope.launch {

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
            serverCode.data.forEach {
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
           setState(SplashState.GetBookMaker(bookmark))
        }
    }

    fun getArticleKeyword(keyword: Int, pass: ArrayList<String>): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticleKeyword(keyword, 1, pass)

        if (serverCode != null) {
            setState(SplashState.GetArticle(serverCode, keyword))

        } else {
            setState(SplashState.GetArticleFail)
        }
    }

    private fun setState(state: SplashState) {
        _profileSplashStateLiveData.postValue(state)
    }
}