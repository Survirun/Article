package com.devlog.date.response

import java.io.Serializable

data class BookmarkResponse(val status: Boolean, val data: List<Article>) : Serializable {
    fun toEntity(): BookmarkResponse = BookmarkResponse(status = status, data = data)
}