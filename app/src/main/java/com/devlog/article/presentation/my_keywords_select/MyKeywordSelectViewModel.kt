package com.devlog.article.presentation.my_keywords_select

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.domain.usecase.user.PostPathMyKeywordsUseCase
import com.devlog.article.presentation.my_keywords_select.intent.MyKeywordSelectIntent
import com.devlog.article.presentation.my_keywords_select.state.MyKeywordSelectApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyKeywordSelectViewModel @Inject constructor(
    private val postPathMyKeywordsUseCase: PostPathMyKeywordsUseCase
):ViewModel(){

    private val _apiState = MutableStateFlow<MyKeywordSelectApiState>(MyKeywordSelectApiState.keywordLoading)
    val apiState: StateFlow<MyKeywordSelectApiState> = _apiState

    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count
    fun processIntent(intent: MyKeywordSelectIntent) {
        when (intent) {
            is MyKeywordSelectIntent.postMyKeywordSelectPost -> {
                pathMyKeywords(intent.keywords)


            }


        }
    }

    fun pathMyKeywords(keywords: Array<Int>):Job =viewModelScope.launch{
        postPathMyKeywordsUseCase.execute(keywords = keywords, onComplete = {}, onException = {}, onError = {}).collect{
            _apiState.value = MyKeywordSelectApiState.postKeywordSuccess
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