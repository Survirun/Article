package com.devlog.article.presentation.app_widget_provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.response.Article
import com.devlog.article.domain.usecase.GetArticleUseCase
import com.devlog.article.presentation.splash.SplashState
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class WidgetViewModel  @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase
) {


    var articleSplashStateLiveData= arrayListOf<Article>()

    fun fetchData(){
        getArticle(1)
    }

    fun getArticle(page: Int = 1): Job = CoroutineScope(Dispatchers.IO).launch() {
        getArticleUseCase.execute(page = page, onError = {

        }, onException = {

        }, onComplete = {

        }).collect {
            articleSplashStateLiveData = (( it.data.articles))

        }


    }
}