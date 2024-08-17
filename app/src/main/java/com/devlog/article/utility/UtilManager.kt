package com.devlog.article.utility

import com.google.gson.GsonBuilder

object UtilManager {

    fun Any.toJson(): String {
        return "\n" + GsonBuilder().setPrettyPrinting().create().toJson(this)
    }
}