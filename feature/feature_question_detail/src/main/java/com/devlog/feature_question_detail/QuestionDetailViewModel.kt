package com.devlog.feature_question_detail

import androidx.lifecycle.ViewModel
import com.devlog.model.data.entity.response.quiz.Question
import com.devlog.model.data.entity.response.quiz.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewModel @Inject constructor() : ViewModel() {
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

    private val _userCheckResponse = MutableStateFlow<String>("")
    val userCheckResponse : MutableStateFlow<String> get() = _userCheckResponse

    private val _question = MutableStateFlow<Question>(dummyQuestion)
    val question :MutableStateFlow<Question> get() = _question

    private val _progress = MutableStateFlow<Float>(0.0f)
    val progress :MutableStateFlow<Float> get() = _progress

    var onQuestionComplete: () -> Unit = {}


    fun updateQuiz(quiz: Quiz){
        _questionList.value =  quiz.questions

        _question.value= _questionList.value[ currentQuestionIndex.value]
        _questionAnswer.value = _questionList.value[ currentQuestionIndex.value].answer
    }
    fun questionCorrectAnswer( optionText:String){
        _userCheckResponse.value = optionText
        setAnswerCorrect(optionText.trim() == questionAnswer.value.trim())


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
            _progress.value =_currentQuestionIndex.value / _questionList.value.size.toFloat()

        } else {
            onQuestionComplete()
        }
    }
}