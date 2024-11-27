package com.devlog.feature_question_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.domain.usecase.question.GetQuestionUseCase
import com.devlog.model.data.entity.question.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    private val getQuestionUseCase: GetQuestionUseCase
) : ViewModel() {
    private val _questionList = MutableStateFlow<List<Question>>(arrayListOf())
    val questionList: StateFlow<List<Question>> get() = _questionList

    private val _questionAnswer = MutableStateFlow<String>("")
    val questionAnswer : StateFlow<String> get() = _questionAnswer


    private val _isAnswerFalseCorrect = MutableStateFlow<Boolean>(false)
    val isAnswerFalseCorrect : MutableStateFlow<Boolean> get() = _isAnswerFalseCorrect

    private val _isAnswerTrueCorrect = MutableStateFlow<Boolean>(false)
    val isAnswerTrueCorrect : MutableStateFlow<Boolean> get() = _isAnswerTrueCorrect

    private val _currentQuestionIndex = MutableStateFlow<Int>(0)
    val currentQuestionIndex :MutableStateFlow<Int> get() = _currentQuestionIndex

    val dummyQuestion = Question("0","","", listOf("","","",""),"","")



    private val _question = MutableStateFlow<Question>(dummyQuestion)
    val question :MutableStateFlow<Question> get() = _question

    var onQuestionComplete: () -> Unit = {}
    fun getQuestionUseCase(day: Int): Job = viewModelScope.launch() {

        getQuestionUseCase.execute(
            day = day,
            onComplete = {},
            onError = {},
            onException = {}
        ).collect {
            _questionList.value =  it
            _question.value= _questionList.value[ currentQuestionIndex.value]
            _questionAnswer.value = _questionList.value[ currentQuestionIndex.value].answer
        }
    }


    fun questionCorrectAnswer( optionText:String){
        setAnswerCorrect(optionText == questionAnswer.value)

    }

    fun setAnswerCorrect(isCorrect: Boolean) {
        _isAnswerTrueCorrect.value = isCorrect
        _isAnswerFalseCorrect.value = !isCorrect

    }

    fun clearAnswerStatus() {
        if (isAnswerTrueCorrect.value){
            moveToNextQuestion()
        }
        _isAnswerTrueCorrect.value = false
        _isAnswerFalseCorrect.value = false

    }

    fun moveToNextQuestion() {
        if (currentQuestionIndex.value < questionList.value.size - 1) {
            currentQuestionIndex.value++
            _question.value= _questionList.value[ currentQuestionIndex.value]
            _questionAnswer.value = _questionList.value[ currentQuestionIndex.value].answer
        } else {
            onQuestionComplete()
        }
    }
}