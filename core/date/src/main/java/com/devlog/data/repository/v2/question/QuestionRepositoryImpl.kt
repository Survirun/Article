package com.devlog.data.repository.v2.question

import com.devlog.model.data.entity.response.quiz.QuizResponse
import com.devlog.network.DataSource
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class QuestionRepositoryImpl2  @Inject constructor(
    private val network: DataSource
) : QuestionRepository2 {



    override suspend fun getTitlesLIstAll(): ApiResponse<QuizResponse> {
       return network.getQuiz()
    }

}