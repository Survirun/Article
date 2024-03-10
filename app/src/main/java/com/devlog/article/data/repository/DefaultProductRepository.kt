package com.devlog.article.data.repository



import android.annotation.SuppressLint
import android.graphics.pdf.PdfDocument.Page
import android.util.Log
import com.devlog.article.data.entity.ArticleLogEntity
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.entity.MyKeyword
import com.devlog.article.data.entity.Passed
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.BookmarkResponse
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRepository private constructor(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
): Repository {
    companion object {

        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: DefaultRepository? = null
        fun getInstance( api: ApiService,ioDispatcher: CoroutineDispatcher): DefaultRepository {
            if (instance == null) instance = DefaultRepository(api,ioDispatcher)
            return instance as DefaultRepository
        }
    }
    override suspend fun postLogin(loginEntity: LoginEntity):DefaultResponse? = withContext(ioDispatcher) {
        val response=api.postLogin(loginEntity)
        return@withContext if (response.isSuccessful){
            response.body()
        }else{
            null
        }
    }

    override suspend fun pathMyKeywords(keywords: Array<Int>): Boolean = withContext(ioDispatcher){
        val response=api.pathMyKeywords(MyKeyword(keywords))
        return@withContext response.isSuccessful
    }

    override suspend fun getUserInfo(): UserInfoEntity? = withContext(ioDispatcher) {
        val response=api.getUserInfo()
        return@withContext if (response.isSuccessful){
             response.body()?.toEntity()
        }else{
            null
        }
    }

    override suspend fun getArticle(page:Int,passed:ArrayList<String>): ArticleResponse? = withContext(ioDispatcher) {
        val response=api.getArticle(1, Passed(passed))
        return@withContext if (response.isSuccessful){
            response.body()
        }else{
            null
        }
    }

    override suspend fun postBookmark(articleId:String): Boolean = withContext(ioDispatcher) {
        val response=api.postBookmark(articleId)
        return@withContext response.isSuccessful
    }

    override suspend fun getBookMaker(): BookmarkResponse? = withContext(ioDispatcher){
        val response=api.getBookMaker()
        return@withContext  response.body()
    }

    override suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>): Boolean= withContext(ioDispatcher) {
        val response=api.postArticleLog(ArticleLogEntity(articleLogResponse))
        return@withContext response.isSuccessful
    }

}