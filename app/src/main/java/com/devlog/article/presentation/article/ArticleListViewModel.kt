package com.devlog.article.presentation.article

import android.util.Log
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
import com.devlog.article.utility.UtilManager.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ArticleListViewModel@Inject constructor(
    private val getArticleKeywordUseCase : GetArticleKeywordUseCase
): ViewModel(

) {
    var userSignCheck =true
    var permission =""
    lateinit var succeed: () -> Unit
    lateinit var failed: () -> Unit
    lateinit var reportSucceed: () -> Unit
    lateinit var reportFailed: () -> Unit
    lateinit var article: ArrayList<Article>
    lateinit var bookmark: ArrayList<ArticleEntity>

    lateinit var test: () -> Unit
    lateinit var test1: () -> Unit

    fun getArticle(passed: ArrayList<String>, page : Int): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle(page, passed)
        if (serverCode != null) {
            article = serverCode.data.articles as ArrayList<Article>
            succeed()

        } else {
            failed()
        }


    }
    fun getArticleKeyword(page: Int, keyword: Int, pass: ArrayList<String>): Job = viewModelScope.launch {
        val articleKeywordRequest= ArticleKeywordRequest(page,keyword,pass)
        getArticleKeywordUseCase.execute(
            articleKeywordRequest,
            onComplete = {},
            onError = {
                failed()
            },
            onException = {
                failed()
            }
        ).collect {
           Log.d("박태규", "getAppTechInfo : ${it.toJson()}")
            succeed()

        }
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

//        val repository: ArticleRepository =
//            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
//        val serverCode = repository.getArticleKeyword(keyword, page, pass)
//
//        if (serverCode != null) {
//            article = serverCode.data.articles as ArrayList<Article>
//            succeed()
//
//        } else {
//            failed()
//        }
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
            failed()
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

}