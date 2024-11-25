package com.devlog.article.data.repository.v3

import com.devlog.article.data.network.DataSource
import com.devlog.article.data.request.ArticleKeywordRequest
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.ArticleSeveralKeywordResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
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
