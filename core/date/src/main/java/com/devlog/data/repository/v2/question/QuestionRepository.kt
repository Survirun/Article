package com.devlog.data.repository.v2.question

import com.devlog.model.data.entity.response.quiz.QuizResponse
import com.skydoves.sandwich.ApiResponse

interface QuestionRepository2 {

    suspend fun getTitlesLIstAll():ApiResponse<QuizResponse>
}
