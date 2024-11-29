package com.devlog.domain.usecase.question

import android.util.Log
import com.devlog.data.repository.v2.question.QuestionRepository2
import com.devlog.model.data.entity.response.quiz.QuizResponse
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

class GetQuestionAllUseCase @Inject constructor(private val questionRepository: QuestionRepository2){

    suspend fun execute(
        onComplete: () -> Unit,
        onError: (Response) -> Unit,
        onException: (Throwable) -> Unit
    ): Flow<QuizResponse> {

        return flow {
            val response = questionRepository.getTitlesLIstAll()

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