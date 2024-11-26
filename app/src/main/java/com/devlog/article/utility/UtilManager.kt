package com.devlog.article.utility

import com.google.gson.GsonBuilder
import java.net.HttpURLConnection
import java.net.URL
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
    fun isImageUrlValid(urlString: String): Boolean {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000  // 5초 타임아웃
            connection.readTimeout = 5000  // 5초 타임아웃
            connection.connect()

            // HTTP 응답 코드가 200(성공)인 경우 유효한 URL
            connection.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            // 예외가 발생하면 유효하지 않은 URL
            false
        }
    }


}