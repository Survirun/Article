package com.devlog.article.presentation.my_keywords_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.ArticleRepository
import com.devlog.article.data.repository.ArticleRepositoryImpl
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.UserRepository
import com.devlog.article.data.response.ArticleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



class MyKeywordSelectViewModel :ViewModel(){
    lateinit var failed :()->Unit
    lateinit var succeedPathMyKeywords:()-> Unit

    fun pathMyKeywords(keywords: Array<Int>):Job =viewModelScope.launch{
        val api= ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: UserRepository = DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode= repository.pathMyKeywords(keywords)
        if (serverCode){
            succeedPathMyKeywords()

        }else{
            failed()
        }

    }




}