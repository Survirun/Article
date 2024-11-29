package com.devlog.question_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.domain.usecase.question.GetQuestionAllUseCase
import com.devlog.model.data.entity.response.quiz.Quiz
import com.devlog.question_list.intent.QuestionIntent
import com.devlog.question_list.state.QuestionApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionTitleListUseCase: GetQuestionAllUseCase
) : ViewModel() {
    private val _questionTitleLis = MutableStateFlow<QuestionApiState>(QuestionApiState.Initialize)
    val questionTitleLis: MutableStateFlow<QuestionApiState> = _questionTitleLis

    private val _quizList =  MutableStateFlow<List<Quiz>>(listOf())
    val  quizList : MutableStateFlow<List<Quiz>> = _quizList
     fun  processIntent(questionIntent: QuestionIntent){
        when(questionIntent){
            is QuestionIntent.Initialize->{

            }
            is QuestionIntent.GetQuestion->{
                getQuestionTitleList()
            }
        }



    }

     fun getQuestionTitleList() : Job = viewModelScope.launch() {
        getQuestionTitleListUseCase.execute(
            onComplete = {},
            onError = {},
            onException = {}
        ).collect{ result ->
            Log.d("polaris",result.toString())
            _quizList.value = result.data.quizes
            _questionTitleLis.value = QuestionApiState.QuestionSuccess(result)
        }
    }




}