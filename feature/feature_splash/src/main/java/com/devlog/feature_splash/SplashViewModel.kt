package com.devlog.feature_splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.presentation.splash.intent.SplashIntent
import com.devlog.model.data.entity.response.Data
import com.devlog.domain.usecase.article.GetArticleKeywordUseCase2
import com.devlog.domain.usecase.article.GetArticleSeveralKeywordUseCase2
import com.devlog.domain.usecase.article.GetArticleUseCase2
import com.devlog.domain.usecase.article.GetBookMakerUseCase2
import com.devlog.feature_splash.state.SplashApiState
import com.devlog.feature_splash.state.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel2 @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase2,
    private val getArticleKeywordUseCase: GetArticleKeywordUseCase2,
    private val getArticleSeveralKeywordUseCase: GetArticleSeveralKeywordUseCase2,
    private val getBookMakerUseCase: GetBookMakerUseCase2
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace() // throwable = SocketException or HttpException or UnknownHostException or else
    }
    private val _isUiFinished = MutableStateFlow<Boolean>(false)
    val isUiFinished: StateFlow<Boolean> = _isUiFinished

    var isApiFinished by mutableStateOf(false)
    private var apiCalled = false
    fun fetchDataOnce() {
        Log.e("polaris","adfsafsd")
        if (!apiCalled) {
            apiCalled = true
            Log.e("polaris","adfsafsd")

        }
    }



    var article:Map<String, Data> = mapOf()
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

                val getApiKeywordList = arrayListOf(12,10,3,9,4,5,6,7,8)
                getArticleSeveralKeyword(getApiKeywordList)


            }
            is SplashIntent.GetBookMaker->{
                getBookMaker()

            }


            else -> {}
        }
    }



    fun getArticleSeveralKeyword(keywordList:ArrayList<Int>): Job =
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
            Log.d("polaris_0428","polaris_0428")


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
             Log.d("polaris","010")
             isApiFinished = true
         }
        if (count==3){
            _uiState.value = SplashUiState.Success
        }

    }

    fun test(){
        _isUiFinished.value =true
    }


}