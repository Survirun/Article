package com.devlog.article.data.repository

import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.UserInfoEntity


interface Repository {
    suspend fun postLogin(loginEntity: LoginEntity):Int

    suspend fun pathMyKeywords(keywords: Array<String>): Boolean

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