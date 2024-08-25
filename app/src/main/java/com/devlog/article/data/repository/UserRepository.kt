package com.devlog.article.data.repository

import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.response.DefaultResponse
import com.devlog.article.data.response.UserInfoEntity


interface UserRepository {



    suspend fun pathMyKeywords(keywords: Array<Int>): Boolean

    suspend fun getUserInfo(): UserInfoEntity?
    suspend fun deleteUser(): Int
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