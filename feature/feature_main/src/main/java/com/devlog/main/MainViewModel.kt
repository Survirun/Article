package com.devlog.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.presentation.main.state.MainApiState
import com.devlog.date.entity.article.LoginEntity
import com.devlog.domain.usecase.article.GetArticleKeywordUseCase
import com.devlog.domain.usecase.article.GetArticleUseCase
import com.devlog.domain.usecase.article.postArticleLogUseCase
import com.devlog.domain.usecase.user.PostLoginUseCase
import com.devlog.feature_article_list.state.ArticleTabState
import com.devlog.main.intent.MainIntent
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.Article
import com.devlog.model.data.entity.response.ArticleLogResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase : GetArticleKeywordUseCase,
    private val postLoginUseCase: PostLoginUseCase,
    private val postArticleLogUseCase: postArticleLogUseCase
):ViewModel() {

    private val _apiState = MutableStateFlow<MainApiState>(MainApiState.Initialize)
    val apiState : StateFlow<MainApiState> = _apiState

    var articleArray: MutableState<ArrayList<ArticleTabState>> = mutableStateOf(arrayListOf())
    var article= MutableLiveData<ArrayList<Article>>()
    var articles = ArrayList<ArticleTabState>()

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.PostSignIn->{
                postSignIn(uid = intent.uid, email = intent.email, name = intent.name)
            }



        }
    }
     fun postSignIn(uid:String,email:String,name:String):Job =viewModelScope.launch {

        postLoginUseCase.execute(
            LoginEntity(uid = uid , email =email ,name=name),
            onError = {
                Log.d("polaris_onError",it.toString())
                _apiState.value = MainApiState.SignInError(it.code.toString())

            },
            onException = {
                Log.d("polaris_onException",it.toString())
              _apiState.value = MainApiState.SignIException(it.message.toString())
            },
            onComplete = {

            }
        ).collect {
            _apiState.value = MainApiState.SignInSuccess


        }




    }
    fun getArticle(page : Int): Job = viewModelScope.launch {
        getArticleUseCase.execute(page = page, onError = {
            Log.d("polaris_onError",it.toString())
        }, onException = {
            Log.d("polaris_onError",it.toString())
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
            postArticleLogUseCase.execute(articleLogResponse = postArticleLogResponse, onComplete = {}, onError = {}, onException = {})

        }

}