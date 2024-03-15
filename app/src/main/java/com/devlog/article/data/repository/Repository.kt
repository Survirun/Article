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


interface Repository {

    suspend fun postLogin(loginEntity: LoginEntity): DefaultResponse?

    suspend fun pathMyKeywords(keywords: Array<Int>): Boolean

    suspend fun getUserInfo():UserInfoEntity?

    suspend fun getArticle(page:Int,passed:ArrayList<String>):ArticleResponse?

    suspend fun postBookmark(articleId:String):Boolean

    suspend fun getBookMaker(): BookmarkResponse?

    suspend fun postArticleLog(articleLogResponse: ArrayList<ArticleLogResponse>):Boolean

    suspend fun postReport(articleId: String): Boolean
//
//    suspend fun getLocalProductList():List<ProductEntity>
//
//    suspend fun insertProductItem(ProductItem:ProductEntity):Long
//
//    suspend fun insertProductList(ProductList: List<ProductEntity>)
//
//    suspend fun updateProductItem(ProductItem:ProductEntity)
//
//    suspend fun getProductItem(itemId:Long):ProductEntity?
//
//    suspend fun deleteAll()
//
//    suspend fun deleteProductItem(id:Long)


}