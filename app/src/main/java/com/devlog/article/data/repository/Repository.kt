package com.devlog.article.data.repository

import android.annotation.SuppressLint
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.UserInfoEntity
import kotlinx.coroutines.CoroutineDispatcher
import java.util.ArrayList


interface Repository {

    suspend fun postLogin(loginEntity: LoginEntity):Int

    suspend fun pathMyKeywords(keywords: Array<Int>): Boolean

    suspend fun getUserInfo():UserInfoEntity?

    suspend fun getArticle():ArticleResponse?
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