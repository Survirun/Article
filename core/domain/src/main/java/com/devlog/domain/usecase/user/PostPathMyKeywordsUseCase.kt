package com.devlog.domain.usecase.user

import android.util.Log
import com.devlog.data.repository.v3.UserRepository
import com.devlog.date.response.DefaultResponse
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

class PostPathMyKeywordsUseCase  @Inject constructor(
    private val apiRepository: UserRepository
) {
    suspend fun execute(
        keywords: Array<Int>,
        onComplete: () -> Unit,
        onError: (Response) -> Unit,
        onException: (Throwable) -> Unit
    ): Flow<DefaultResponse> {

        return flow {
            val response = apiRepository.pathMyKeywords(keywords)
            response.suspendOnSuccess {
                emit(data)
            }.suspendOnError {
                Log.d("polaris", "onError : ${this}")
                Log.d("polaris", "raw : ${raw}")
                onError(raw)
            }.suspendOnException {
                Log.d("polaris", "onError : ${this}")
                Log.d("polaris", exception.toString())
                onException(exception)
            }
        }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
    }
}