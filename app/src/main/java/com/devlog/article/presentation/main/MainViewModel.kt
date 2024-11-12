package com.devlog.article.presentation.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.ArticleRepository
import com.devlog.article.data.repository.ArticleRepositoryImpl
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.domain.usecase.article.GetArticleKeywordUseCase
import com.devlog.article.domain.usecase.article.GetArticleUseCase
import com.devlog.article.presentation.article.ArticleTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase : GetArticleKeywordUseCase):ViewModel() {


    var articleArray: MutableState<ArrayList<ArticleTabState>> = mutableStateOf(arrayListOf())
    var article= MutableLiveData<ArrayList<Article>>()
    var articles = ArrayList<ArticleTabState>()


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

}