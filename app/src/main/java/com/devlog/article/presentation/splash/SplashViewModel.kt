package com.devlog.article.presentation.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.response.Data
import com.devlog.article.domain.usecase.GetArticleKeywordUseCase
import com.devlog.article.domain.usecase.GetArticleSeveralKeywordUseCase
import com.devlog.article.domain.usecase.GetArticleUseCase
import com.devlog.article.domain.usecase.GetBookMakerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase: GetArticleKeywordUseCase,
    private val getArticleSeveralKeywordUseCase: GetArticleSeveralKeywordUseCase,
    private val getBookMakerUseCase: GetBookMakerUseCase
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


    lateinit var article:Map<String, Data>




    private var _profileSplashStateLiveData = MutableLiveData<SplashState>(SplashState.Initialize)
    val profileSplashStateLiveData: LiveData<SplashState> = _profileSplashStateLiveData

    fun fetchData() = viewModelScope.launch {
        setState(SplashState.Loading)
    }

    fun getArticleSeveralKeyword(keywordList: List<Int>): Job =
        viewModelScope.launch ( coroutineExceptionHandler ) {

            getArticleSeveralKeywordUseCase.execute(keywordList = keywordList, page = 1, onError = {

            }, onException = {

            }, onComplete = {


            }).collect {
                _profileSplashStateLiveData.postValue(SplashState.GetArticleKeyword(it.data))
            }
        }

    fun getArticle(page: Int = 1): Job = viewModelScope.launch(coroutineExceptionHandler) {
        getArticleUseCase.execute(page = page, onError = {

        }, onException = {

        }, onComplete = {

        }).collect {
            _profileSplashStateLiveData.postValue(SplashState.GetArticle( mapOf("0" to it.data)))

        }


    }

    fun getBookMaker(): Job = viewModelScope.launch(coroutineExceptionHandler) {
        getBookMakerUseCase.execute(onComplete = {

        }, onError = {

        }, onException = {

        }
        ).collect{
            setState(SplashState.GetBookMaker(it.data))
        }


    }





    private fun setState(state: SplashState) {
        _profileSplashStateLiveData.postValue(state)
    }
}