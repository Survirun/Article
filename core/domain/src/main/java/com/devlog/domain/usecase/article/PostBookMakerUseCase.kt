package com.devlog.domain.usecase.article

import android.util.Log
import com.devlog.data.repository.v3.ArticleRepository2
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

class PostBookMakerUseCase  @Inject constructor(
    private val apiRepository: ArticleRepository2
) {
    suspend fun execute(
        articleId:String,
        onComplete: () -> Unit,
        onError: (Response) -> Unit,
        onException: (Throwable) -> Unit
    ): Flow<DefaultResponse> {

        return flow {
            val response = apiRepository.postBookmark(articleId)
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