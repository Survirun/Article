package com.devlog.article.utility

import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object UtilManager {

    fun Any.toJson(): String {
        return "\n" + GsonBuilder().setPrettyPrinting().create().toJson(this)
    }
    fun getToday(){
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return getToday()
    }

    fun getTodayToInt():Int{
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return  getToday().toString().toInt()
    }
}