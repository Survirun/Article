package com.devlog.article.presentation.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

class SplashViewModel() : ViewModel() {

    var isApiFinished by mutableStateOf(false)


    init {
        // Simulate API call
        viewModelScope.launch {

            isApiFinished = false
        }
    }
    lateinit var failed: () -> Unit
    lateinit var keyword_failed: () -> Unit
    lateinit var article: ArticleResponse
    val bookmark = ArrayList<ArticleEntity>()
    private val stateQueue: ConcurrentLinkedQueue<SplashState> = ConcurrentLinkedQueue()
    private var isProcessing = false
    private val maxCount = 11
    private var count = 0

    private var _profileSplashStateLiveData = MutableLiveData<SplashState>(SplashState.Loading)
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
                        articleId = it._id,
                        type = it.type
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
            count++
            enqueueState(SplashState.GetArticle(serverCode, keyword))

        } else {
            enqueueState(SplashState.GetArticleFail)
        }
    }


    private fun enqueueState(state: SplashState) {
        stateQueue.add(state)
        processQueue()
    }

    @Synchronized
    private fun processQueue() {
        if (isProcessing) return
        isProcessing = true

        viewModelScope.launch {
            while (stateQueue.isNotEmpty()) {
                val state = stateQueue.poll()
                state?.let {
                    _profileSplashStateLiveData.postValue(it)
                    delay(150)  // 각 상태 변화 간격 조정
                }
            }
            isProcessing = false
        }
    }
    private fun setState(state: SplashState) {
        _profileSplashStateLiveData.postValue(state)
    }
}