package com.devlog.article.data.repository



import com.devlog.article.data.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultProductRepository(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
): Repository {

}