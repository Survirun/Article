package com.devlog.question_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.domain.usecase.question.GetQuestionTitleListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionTitleListUseCase: GetQuestionTitleListUseCase
) : ViewModel() {
    private val _questionTitleLis = MutableStateFlow<List<String>>(arrayListOf())
    val questionTitleLis: StateFlow<List<String>> get() = _questionTitleLis

     fun getQuestionTitleList() : Job = viewModelScope.launch() {
        getQuestionTitleListUseCase.execute(
            onComplete = {},
            onError = {},
            onException = {}
        ).collect{ result ->
            Log.d("polaris",result::class.simpleName.toString())
            _questionTitleLis.value = result
        }
    }


}