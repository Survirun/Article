package com.devlog.article.presentation.article

import android.util.Log
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableIntStateOf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.article.ArticleEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit


import com.devlog.article.data.repository.ArticleRepository
import com.devlog.article.data.repository.ArticleRepositoryImpl
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.UserRepository
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.domain.usecase.article.GetArticleKeywordUseCase
import com.devlog.article.domain.usecase.article.GetArticleUseCase
import com.devlog.article.data.entity.article.Common
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.presentation.article.intent.ArticleIntent
import com.devlog.article.presentation.article.state.ArticleApiState
import com.devlog.article.presentation.article.state.ArticleTabState
import com.devlog.article.presentation.my_keywords_select.intent.MyKeywordSelectIntent
import com.devlog.article.presentation.my_keywords_select.state.MyKeywordSelectApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ArticleListViewModel@Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase : GetArticleKeywordUseCase
): ViewModel(

) {
    var userSignCheck = PrefManager.userSignInCheck
    var permission =""

    private val _apiState = MutableStateFlow<ArticleApiState>(ArticleApiState.Initialize)
    val apiState :StateFlow<ArticleApiState> = _apiState


    fun processIntent(intent: ArticleIntent) {
        when (intent) {
            is ArticleIntent.getArticle->{
                getArticle(intent.page)
            }
            is  ArticleIntent.getArticleKeyword->{
                getArticleKeyword(intent.page,intent.keyword)
            }
            is ArticleIntent.postDeleteAccount ->{
                deleteUser()
            }


            else -> {}
        }
    }

    lateinit var reportSucceed: () -> Unit
    lateinit var reportFailed: () -> Unit




    lateinit var currentArticles: MutableState<ArticleTabState>
    var tabIndex= mutableIntStateOf(0)

    var articles = ArrayList<ArticleTabState>()
    var userViewArticleId = arrayListOf<String>()

    fun getArticle(page : Int): Job = viewModelScope.launch {
        getArticleUseCase.execute(page = page, onError = {

        }, onException = {

        }, onComplete = {

        }).collect{

            _apiState.value = ArticleApiState.GetArticleSuccess(it.data.articles)

        }





    }
    fun getArticleKeyword(page: Int, keyword: Int): Job = viewModelScope.launch {
        val articleKeywordRequest= ArticleKeywordRequest(keyword=keyword,page=page)
        getArticleKeywordUseCase.execute(
            articleKeywordRequest,
            onComplete = {},
            onError = {
               Log.d("polaris_onError",it.toString())
            },
            onException = {
                Log.d("polaris_onException",it.toString())
            }
        ).collect {
            _apiState.value = ArticleApiState.GetArticleKeywordSuccess(it.data.articles)


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
            val repository: ArticleRepository =
                ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
            val serverCode = repository.postArticleLog(postArticleLogResponse)
            if (serverCode != null) {

            } else {
            }

        }

    fun postBookmark(articleId: String): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.postBookmark(articleId)
        if (serverCode) {

        } else {

        }

    }

    fun postReport(articleId: String): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.postReport(articleId)
        if (serverCode) {
            reportSucceed()
        } else {
            reportFailed()
        }

    }

    fun deleteUser(): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: UserRepository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.deleteUser()
        if (serverCode==200) {
            _apiState.value = ArticleApiState.PostDeleteAccountSuccess
        } else {
            _apiState.value = ArticleApiState.PostDeleteAccountFail
        }
    }

    fun addArticles(articleTabState: ArticleTabState) {
        articleTabState.page += 1
        if (userSignCheck && articleTabState.keyword == Common) {
            processIntent(ArticleIntent.getArticle(articleTabState.page))
            getArticle(articleTabState.page)
        } else {
            processIntent(ArticleIntent.getArticleKeyword(articleTabState.page, articleTabState.keyword))

        }


    }
    fun isAdmin(): Boolean {

        return permission == "admin"
    }

    fun stateComplete(){
        _apiState.value  =  ArticleApiState.Initialize
    }
}