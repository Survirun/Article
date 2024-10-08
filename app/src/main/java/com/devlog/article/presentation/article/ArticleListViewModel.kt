package com.devlog.article.presentation.article

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.UserRepository
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.domain.usecase.GetArticleKeywordUseCase
import com.devlog.article.domain.usecase.GetArticleUseCase
import com.devlog.article.presentation.my_keywords_select.Common
import com.devlog.article.utility.UtilManager.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ArticleListViewModel@Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase : GetArticleKeywordUseCase
): ViewModel(

) {
    var userSignCheck =true
    var permission =""

    lateinit var reportSucceed: () -> Unit
    lateinit var reportFailed: () -> Unit
    var article=MutableLiveData<ArrayList<Article>>()
    lateinit var bookmark: ArrayList<ArticleEntity>

    lateinit var test: () -> Unit
    lateinit var test1: () -> Unit

    lateinit var currentArticles:MutableState<ArticleTabState>
    var tabIndex= mutableIntStateOf(0)

    var articles = ArrayList<ArticleTabState>()
    var userViewArticleId = arrayListOf<String>()

    fun getArticle(page : Int): Job = viewModelScope.launch {
        getArticleUseCase.execute(page = page, onError = {

        }, onException = {

        }, onComplete = {

        }).collect{
            article.value = it.data.articles

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
           Log.d("polaris", "getAppTechInfo : ${it.toJson()}")
            article.value=it.data.articles


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
            test()
        } else {
            test1()
        }
    }

    fun addArticles(articleTabState: ArticleTabState) {
        articleTabState.page += 1
        if (userSignCheck && articleTabState.keyword == Common) {
            getArticle(articleTabState.page)
        } else {
            getArticleKeyword(articleTabState.page, articleTabState.keyword)
        }


    }
    fun isAdmin(): Boolean {

        return permission == "admin"
    }

}