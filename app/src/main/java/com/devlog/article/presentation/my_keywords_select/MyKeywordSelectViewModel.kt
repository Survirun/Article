package com.devlog.article.presentation.my_keywords_select

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.LoginBuildOkHttpClient
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.Repository
import com.devlog.article.data.response.ArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MyKeywordSelectViewModel :ViewModel(){
    lateinit var succeed :()->Unit
    lateinit var failed :()->Unit
    lateinit var succeedPathMyKeywords:()-> Unit
    lateinit var succeedGetArticle:()->Unit
    lateinit var article: ArticleResponse
    fun pathMyKeywords(keywords: Array<Int>):Job =viewModelScope.launch{
        val api= ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: Repository = DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode= repository.pathMyKeywords(keywords)
        if (serverCode){
            succeedPathMyKeywords()

        }else{
            failed()
        }

    }
    fun getArticle(page:ArrayList<String>): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: Repository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle(1,page )
        if (serverCode!=null) {
            article=serverCode
            succeedGetArticle()

        } else {
            failed()
        }


    }
}