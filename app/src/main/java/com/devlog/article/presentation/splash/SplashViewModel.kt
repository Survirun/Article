package com.devlog.article.presentation.splash

import android.util.Log
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
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.domain.usecase.GetArticleKeywordUseCase
import com.devlog.article.domain.usecase.GetArticleSeveralKeywordUseCase
import com.devlog.article.domain.usecase.GetArticleUseCase
import com.devlog.article.utility.UtilManager.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase: GetArticleKeywordUseCase,
    private val getArticleSeveralKeywordUseCase: GetArticleSeveralKeywordUseCase
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace() // throwable = SocketException or HttpException or UnknownHostException or else
    }
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

    private var isProcessing = false
    private val maxCount = 11
    private var count = 0

    private var _profileSplashStateLiveData = MutableLiveData<SplashState>(SplashState.Initialize)
    val profileSplashStateLiveData: LiveData<SplashState> = _profileSplashStateLiveData

    fun fetchData() = viewModelScope.launch {
        setState(SplashState.Loading)
    }

    fun getArticleSeveralKeyword(keywordList: ArrayList<Int>): Job =
        viewModelScope.launch ( coroutineExceptionHandler ) {

            getArticleSeveralKeywordUseCase.execute(keywordList = keywordList, page = 1, onError = {

            }, onException = {

            }, onComplete = {


            }).collect {
                _profileSplashStateLiveData.postValue(SplashState.GetArticle(it.data))
            }
        }

    fun getArticle(page: Int = 1): Job = viewModelScope.launch(coroutineExceptionHandler) {
        getArticleUseCase.execute(page = page, onError = {

        }, onException = {

        }, onComplete = {

        }).collect {
            article = it
        }


    }

    fun getBookMaker(): Job = viewModelScope.launch(coroutineExceptionHandler) {

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

    fun getArticleKeyword(keyword: Int, pass: ArrayList<String>): Job =
        viewModelScope.launch(coroutineExceptionHandler) {
            val articleKeywordRequest = ArticleKeywordRequest(keyword, 1, pass)


            getArticleKeywordUseCase.execute(
                articleKeywordRequest,
                onComplete = {},
                onError = {
                    _profileSplashStateLiveData.postValue(SplashState.GetArticleFail)
                },
                onException = {
                    _profileSplashStateLiveData.postValue(SplashState.GetArticleFail)
                }
            ).collect {
                count++


            }


        }




    private fun setState(state: SplashState) {
        _profileSplashStateLiveData.postValue(state)
    }
}