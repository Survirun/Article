package com.devlog.article.data.repository.v2.question

import android.util.Log
import com.devlog.data.repository.v2.question.QuestionRepository
import com.devlog.date.entity.question.Week
import com.devlog.model.data.entity.question.Question
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response

class QuestionRepositoryImpl  constructor(
    private val weeks: List<Week>
) : QuestionRepository {

    override suspend fun getQuestionsForDay(day: Int): ApiResponse<List<Question>> {
        for (week in weeks) {
            for (dayData in week.days) {
                if (dayData.day == day) {
                    return ApiResponse.Success(Response.success(dayData.questions))
                }
            }
        }
        // 해당 날짜에 질문이 없으면 ApiResponse.Error로 반환
        return ApiResponse.error(Throwable("error"))
    }

    override suspend fun getTitlesLIst(): ApiResponse<List<String>> {
        val titles = mutableListOf<String>()
        for (week in weeks) {
            Log.d("polaris",week.toString())
            Log.d("polaris",weeks.toString())
            for (dayData in week.days) {
                Log.d("polaris",dayData.title)
                titles.add(dayData.title)
            }
        }
        return ApiResponse.Success(Response.success(titles))

    }

}