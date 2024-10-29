package com.devlog.article.data.repository

import android.app.Application
import com.devlog.article.data.entity.question.Question
import com.devlog.article.data.entity.question.Week

class QuestionRepositoryImpl(
    private val weeks: List<Week>
) : QuestionRepository {

    override suspend fun getQuestionsForDay(day: Int): List<Question>? {
        for (week in weeks) {
            for (dayData in week.days) {
                if (dayData.day == day) {
                    return dayData.questions
                }
            }
        }
        return null // 해당 날짜가 없으면 null 반환
    }
}