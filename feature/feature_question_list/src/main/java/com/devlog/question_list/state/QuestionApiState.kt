package com.devlog.question_list.state

import com.devlog.model.data.entity.response.quiz.QuizResponse

sealed class QuestionApiState {
    data object Initialize : QuestionApiState()
    data class QuestionSuccess(val quizResponse: QuizResponse) : QuestionApiState()

}