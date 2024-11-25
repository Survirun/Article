package com.devlog.date.response

import java.io.Serializable

data class ArticleResponse(val status: Boolean, val data:Data): Serializable {
    fun toEntity(): ArticleResponse = ArticleResponse(status = status, data = data)

}
data class Data(val page:Int,val maxPage:Int,val articles: ArrayList<Article>): Serializable
data class Article(
    var snippet: String?,
    var date: String?,
    var thumbnail: String?,
   // val keywords: ArrayList<String>,
    val displayLink: String,
    val sitename: String,
    val link: String,
    val title: String,
    val cx: Int,
    val _id : String,
    var weight : Double?,
    var type:Int
): Serializable  {

}