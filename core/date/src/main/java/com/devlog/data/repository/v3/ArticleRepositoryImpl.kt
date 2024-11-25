package com.devlog.data.repository.v3

import com.devlog.article.data.repository.v3.ArticleRepository
import com.devlog.date.response.ArticleResponse
import com.devlog.date.response.ArticleSeveralKeywordResponse
import com.devlog.date.response.BookmarkResponse
import com.devlog.date.response.DefaultResponse
import com.devlog.model.data.entity.request.ArticleKeywordRequest
import com.devlog.model.data.entity.response.ArticleLogResponse
import com.devlog.network.DataSource
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

internal class ArticleRepositoryImpl @Inject constructor(
    private val network: DataSource
) : ArticleRepository {
    override suspend fun getArticle(
        page: Int
    ): ApiResponse<ArticleResponse> {
        return  network.getArticle(page)
    }

    override suspend fun postBookmark(articleId: String): ApiResponse<DefaultResponse> {
        return  network.postBookmark(articleId)
    }

    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): ApiResponse<DefaultResponse> {
        return  network.postBookmark(articleLogResponse.toString())
    }

    override suspend fun postReport(articleId: String): ApiResponse<DefaultResponse> {
        return  network.postReport(articleId)
    }

    override suspend fun getArticleKeyword(articleKeywordRequest: ArticleKeywordRequest): ApiResponse<ArticleResponse> {
        return  network.getArticleKeyword(articleKeywordRequest)
    }

    override suspend fun getBookMaker(): ApiResponse<BookmarkResponse> {
        return network.getBookMaker()
    }

    override suspend fun getArticleSeveralKeyword(
        keywords: ArrayList<Int>,
        page: Int
    ): ApiResponse<ArticleSeveralKeywordResponse> {
        return  network.getArticleSeveralKeyword(keywords, page)
    }


}
