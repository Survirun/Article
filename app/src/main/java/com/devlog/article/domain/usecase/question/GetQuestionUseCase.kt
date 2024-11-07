package com.devlog.article.domain.usecase.question

import android.util.Log
import com.devlog.article.data.entity.question.Question
import com.devlog.article.data.repository.QuestionRepository
import com.devlog.article.data.repository.v2.ApiRepository
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import okhttp3.Response
import javax.inject.Inject

class GetQuestionUseCase  @Inject constructor( private val questionRepository: QuestionRepository){

    suspend fun execute(
        day:Int,
        onComplete: () -> Unit,
        onError: (Response) -> Unit,
        onException: (Throwable) -> Unit
    ): Flow<List<Question>> {

        return flow {
            val response = questionRepository.getQuestionsForDay(day )

            response.suspendOnSuccess {
                emit(data)
            }.suspendOnError {
                Log.d("polaris", "onError : ${this}")
                Log.d("polaris", "raw : ${raw}")
                onError(raw)
            }.suspendOnException {
                Log.d("polaris", exception.toString())
                onException(exception)
            }
        }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
    }
}