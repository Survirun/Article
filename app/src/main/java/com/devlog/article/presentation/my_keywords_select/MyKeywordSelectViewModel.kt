package com.devlog.article.presentation.my_keywords_select

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.UserRepository
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
class MyKeywordSelectViewModel @Inject constructor():ViewModel(){

    private val _apiState = MutableStateFlow<MyKeywordSelectApiState>(MyKeywordSelectApiState.keywordLoading)
    val apiState: StateFlow<MyKeywordSelectApiState> = _apiState

    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count
    fun processIntent(intent: MyKeywordSelectIntent) {
        when (intent) {
            is MyKeywordSelectIntent.postMyKeywordSelectPost -> {
                pathMyKeywords(intent.keywords)


            }


            else -> {}
        }
    }

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

            _apiState.value = MyKeywordSelectApiState.postKeywordSuccess
        }else{

        }

    }

    fun countPlus(){
        Log.d("polaris","실행됨")
        _count.value += 1
    }
    fun countMinus(){
        Log.d("polaris","실행됨")
        _count.value -= 1

    }




}