package com.devlog.article.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.domain.usecase.article.PostBookMakerUseCase
import com.devlog.model.data.entity.response.Article
import com.devlog.preference.PrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor( private val postBookMakerUseCase: PostBookMakerUseCase): ViewModel() {
    var articleList= (PrefManager.readFromSharedPreferences())

    fun ada (): Flow<Int> = flowOf(1)
    fun postBookmark(article: Article): Job = viewModelScope.launch {
        postBookMakerUseCase.execute(articleId = article._id, onComplete = {}, onError = {}, onException = {})


    }
}