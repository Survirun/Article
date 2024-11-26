package com.devlog.feature_article_list


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.date.entity.article.Common

import com.devlog.domain.usecase.article.GetArticleKeywordUseCase
import com.devlog.domain.usecase.article.GetArticleUseCase
import com.devlog.domain.usecase.article.PostBookMakerUseCase
import com.devlog.domain.usecase.article.postArticleLogUseCase
import com.devlog.domain.usecase.article.postArticleReportUseCase
import com.devlog.domain.usecase.user.PostDeleteUserUseCase
import com.devlog.feature_article_list.intent.ArticleIntent
import com.devlog.feature_article_list.state.ArticleApiState
import com.devlog.feature_article_list.state.ArticleTabState
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
import com.devlog.preference.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel@Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val getArticleKeywordUseCase : GetArticleKeywordUseCase,
    private val postArticleLogUseCase: postArticleLogUseCase,
    private val postBookMakerUseCase: PostBookMakerUseCase,
    private val postArticleReportUseCase: postArticleReportUseCase,
    private val postDeleteUserUseCase: PostDeleteUserUseCase
): ViewModel(

) {
    var userSignCheck = PrefManager.userSignInCheck
    var permission =""

    private val _apiState = MutableStateFlow<ArticleApiState>(ArticleApiState.Initialize)
    val apiState :StateFlow<ArticleApiState> = _apiState


    fun processIntent(intent: ArticleIntent) {
        when (intent) {
            is ArticleIntent.getArticle->{
                getArticle(intent.page)
            }
            is  ArticleIntent.getArticleKeyword->{
                getArticleKeyword(intent.page,intent.keyword)
            }
            is ArticleIntent.postDeleteAccount ->{
                deleteUser()
            }


            else -> {}
        }
    }





    lateinit var currentArticles: MutableState<ArticleTabState>
    var tabIndex= mutableIntStateOf(0)

    var articles = ArrayList<ArticleTabState>()
    var userViewArticleId = arrayListOf<String>()

    fun getArticle(page : Int): Job = viewModelScope.launch {
        getArticleUseCase.execute(page = page, onError = {
            Log.d("polaris_onError",it.toString())
        }, onException = {
            Log.d("polaris_onException",it.toString())
        }, onComplete = {

        }).collect{

            _apiState.value = ArticleApiState.GetArticleSuccess(it.data.articles)

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
            _apiState.value = ArticleApiState.GetArticleKeywordSuccess(it.data.articles)


        }

    }

    fun postArticleLog(postArticleLogResponse: ArrayList<ArticleLogResponse>): Job = viewModelScope.launch {
            postArticleLogUseCase.execute(postArticleLogResponse, onComplete = {}, onError = {}, onException = {})
        }

    fun postBookmark(articleId: String): Job = viewModelScope.launch {
        postBookMakerUseCase.execute(articleId, onComplete = {}, onError = {}, onException = {})


    }

    fun postArticleReport(articleId: String): Job = viewModelScope.launch {
        postArticleReportUseCase.execute(articleId = articleId, onComplete = {}, onError = {}, onException = {})



    }

    fun deleteUser(): Job = viewModelScope.launch {
        postDeleteUserUseCase.execute(onComplete = {}, onError = {
            _apiState.value = ArticleApiState.PostDeleteAccountFail
        }, onException = {
            _apiState.value = ArticleApiState.PostDeleteAccountFail
        }).collect {
            _apiState.value = ArticleApiState.PostDeleteAccountSuccess
        }
    }


    fun addArticles(articleTabState: ArticleTabState) {
        articleTabState.page += 1
        Log.d("polaris_articleTabState",articleTabState.keyword.toString())
        if (userSignCheck && articleTabState.keyword == Common) {
            processIntent(ArticleIntent.getArticle(articleTabState.page))
            getArticle(articleTabState.page)
        } else {
            processIntent(ArticleIntent.getArticleKeyword(articleTabState.page, articleTabState.keyword))

        }


    }
    fun isAdmin(): Boolean {

        return permission == "admin"
    }

    fun stateComplete(){
        _apiState.value  =  ArticleApiState.Initialize
    }
}