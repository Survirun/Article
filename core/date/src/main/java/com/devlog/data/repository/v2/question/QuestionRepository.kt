package com.devlog.data.repository.v2.question

import com.devlog.model.data.entity.question.Question
import com.skydoves.sandwich.ApiResponse

interface QuestionRepository2 {
    suspend fun getQuestionsForDay(day: Int): ApiResponse<List<Question>>
    suspend fun getTitlesLIst():ApiResponse<List<String>>
}