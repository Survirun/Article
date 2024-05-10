package com.devlog.article.data.repository

import android.annotation.SuppressLint
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import kotlinx.coroutines.CoroutineDispatcher
import java.util.ArrayList


interface ArticleRepository {

    suspend fun getArticle(page:Int,passed:ArrayList<String>):ArticleResponse?

    suspend fun postBookmark(articleId:String):Boolean

    suspend fun getBookMaker(): BookmarkResponse?

    suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>):Boolean

    suspend fun postReport(articleId: String): Boolean

    suspend fun getArticleKeyword(keyword : Int, page: Int, passed: ArrayList<String>) : ArticleResponse?
}