package com.devlog.article.domain.usecase

import android.util.Log
import com.devlog.article.data.repository.v2.ApiDataSource
import com.devlog.article.data.repository.v2.ApiRepository
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleResponse
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

class GetArticleKeywordUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    suspend fun execute(
        articleKeywordRequest: ArticleKeywordRequest,
        onComplete: () -> Unit,
        onError: (Response) -> Unit,
        onException: (Throwable) -> Unit
    ): Flow<ArticleResponse> {


        return flow {
            val response = apiRepository.getArticleKeyword(articleKeywordRequest)
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