package com.devlog.article.presentation.bookmark

import android.content.Context
import android.preference.PreferenceManager
import com.devlog.article.data.response.Article
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class BookmarkSharedPreferencesHelper(context: Context) {

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val gson = GsonBuilder().create()
    companion object {
        private const val KEY_BOOKMARK = "Bookmark"

    }

    fun saveToSharedPreferences(articles: List<Article>){
        val editor = sharedPreferences.edit()

        // Article 리스트를 JSON으로 변환하여 저장
        val json = gson.toJson(articles)
        editor.putString(KEY_BOOKMARK, json)
        editor.apply()
    }

    fun readFromSharedPreferences(): List<Article> {
        // 저장된 JSON 문자열을 가져와서 Article 리스트로 변환
        val bookmark = sharedPreferences.getString(KEY_BOOKMARK, "") ?: ""
        val listType = object : TypeToken<List<Article>>() {}.type
        return gson.fromJson(bookmark, listType) ?: emptyList()
    }
}