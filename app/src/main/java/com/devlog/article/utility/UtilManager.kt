package com.devlog.article.utility

import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object UtilManager {

    fun Any.toJson(): String {
        return "\n" + GsonBuilder().setPrettyPrinting().create().toJson(this)
    }
    fun getToday():String{
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return formattedDate.toString()
    }

    fun getTodayToInt():Int{
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return  getToday().toInt()
    }

    fun String.toDotDateFormat(): String {
        return this.replace("-", ".")
    }
}