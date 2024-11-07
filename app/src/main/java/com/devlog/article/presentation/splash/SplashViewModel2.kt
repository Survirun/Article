package com.devlog.article.presentation.splash

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.response.Data
import com.devlog.article.domain.usecase.article.GetArticleKeywordUseCase
import com.devlog.article.domain.usecase.article.GetArticleSeveralKeywordUseCase
import com.devlog.article.domain.usecase.article.GetArticleUseCase
import com.devlog.article.domain.usecase.article.GetBookMakerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel2 @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase: GetArticleKeywordUseCase,
    private val getArticleSeveralKeywordUseCase: GetArticleSeveralKeywordUseCase,
    private val getBookMakerUseCase: GetBookMakerUseCase
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace() // throwable = SocketException or HttpException or UnknownHostException or else
    }
    var isApiFinished by mutableStateOf(false)
    private var apiCalled = false
    fun fetchDataOnce() {
        Log.e("polaris","adfsafsd")
        if (!apiCalled) {
            apiCalled = true
            Log.e("polaris","adfsafsd")

        }
    }
    init {
        // Simulate API call

        Log.e("polaris","init")
        viewModelScope.launch {

            isApiFinished = false
        }
        processIntent(SplashIntent.GetArticle)
        processIntent(SplashIntent.GetArticleKeywordList)
        processIntent(SplashIntent.GetBookMaker)
    }


    lateinit var article:Map<String, Data>
    var count =0

    private val _apiState = MutableStateFlow<SplashApiState>(SplashApiState.Loading)
    val apiState: StateFlow<SplashApiState> = _apiState

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loding)
    val uiState: StateFlow<SplashUiState> = _uiState



    fun processIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.GetArticle -> {
                getArticle()


            }
            is SplashIntent.GetArticleKeywordList -> {
                val getApiKeywordList = listOf( 12, 10, 3,9, 4, 5, 6, 7, 8)
                getArticleSeveralKeyword(getApiKeywordList)


            }
            is SplashIntent.GetBookMaker->{
                getBookMaker()

            }


            else -> {}
        }
    }



    fun getArticleSeveralKeyword(keywordList: List<Int>): Job =
        viewModelScope.launch ( coroutineExceptionHandler ) {

            getArticleSeveralKeywordUseCase.execute(keywordList = keywordList, page = 1,
                onError = {
                    _apiState.value = SplashApiState.Failure("에러가 발생했습니다 에러 코드 :"+it.code.toString())
            }, onException = {
                    _apiState.value = SplashApiState.Failure("에러가 발생했습니다 에러 코드 :"+it.toString().toString())
            }, onComplete = {


            }).collect {
                _apiState.value = SplashApiState.GetArticleSuccess(it.data)
                onAllApiSuccess()

            }
        }

    fun getArticle(page: Int = 1): Job = viewModelScope.launch(coroutineExceptionHandler) {
        getArticleUseCase.execute(page = page,
            onError = {
                _apiState.value = SplashApiState.Failure("에러가 발생했습니다 에러 코드 :"+it.code.toString())
        }, onException = {
                _apiState.value = SplashApiState.Failure("에러가 발생했습니다 에러 코드 :"+it.toString().toString())
        }, onComplete = {

        }).collect {
            article =  mapOf("0" to it.data)
            onAllApiSuccess()


        }


    }

    fun getBookMaker(): Job = viewModelScope.launch(coroutineExceptionHandler) {
        getBookMakerUseCase.execute(onComplete = {

        }, onError = {

        }, onException = {

        }
        ).collect{
            _apiState.value = SplashApiState.GetBookMakerSuccess(it.data)
        }


    }


     fun onAllApiSuccess() {
        count++
         if (count==2){
             isApiFinished = true
         }
        if (count==3){
            _uiState.value = SplashUiState.Success
        }

    }


}