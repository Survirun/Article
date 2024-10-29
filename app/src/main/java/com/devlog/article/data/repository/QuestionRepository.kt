package com.devlog.article.data.repository

import com.devlog.article.data.entity.question.Question

interface QuestionRepository {
    suspend fun getQuestionsForDay(day: Int): List<Question>?
}