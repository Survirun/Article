package com.devlog.article.data.response

data class ArticleResponse(val status: Boolean, val data: List<Article>) {
    fun toEntity(): ArticleResponse = ArticleResponse(status = status, data = data)

}

data class Article(
    val snippet: String?,
    val data: String?,
    val thumbnail: String?,
    val keywords: ArrayList<String>,
    val displayLink: String,
    val sitename: String,
    val link: String,
    val title: String,
    val cx: Int
) {

}
