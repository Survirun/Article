package com.devlog.article.data.repository

import com.devlog.article.data.entity.question.Question
import com.skydoves.sandwich.ApiResponse

interface QuestionRepository {
    suspend fun getQuestionsForDay(day: Int): ApiResponse<List<Question>>
    suspend fun getTitlesLIst():ApiResponse<List<String>>
}