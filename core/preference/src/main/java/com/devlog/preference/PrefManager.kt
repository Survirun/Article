package com.devlog.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.devlog.model.data.entity.article.ArticleEntity
import com.devlog.model.data.entity.response.Article
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


@SuppressLint("StaticFieldLeak")
object PrefManager {
    private lateinit var context: Context
    private lateinit var pref: SharedPreferences


    private const val KEY_INIT_PREF_MANAGER = "KEY_INIT_PREF_MANAGER"
    val gson = GsonBuilder().create()
    fun init(mContext: Context) {
        context = mContext
        pref = context.getSharedPreferences(KEY_INIT_PREF_MANAGER, Context.MODE_PRIVATE)
    }

    var userUid:String
        get() = pref.getString(PreferenceConstants.USER_UID, "").toString()
        set(v) {
            pref.edit().putString(PreferenceConstants.USER_UID, v).apply()
        }

    var userSignInCheck: Boolean
        get() = pref.getBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, false)
        set(v) {
            pref.edit().putBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, v).apply()
        }

    var userKeywordCheck :Boolean
        get() = pref.getBoolean(PreferenceConstants.USER_KEYWORD_CHECK, false)
        set(v) {
            pref.edit().putBoolean(PreferenceConstants.USER_KEYWORD_CHECK, v).apply()
        }

    var userName: String
        get() = pref.getString(PreferenceConstants.USER_NAME, "")!!
        set(v) {
            pref.edit().putString(PreferenceConstants.USER_NAME, v).apply()
        }

    //앱 마지막 접속 날자
    var appAccessDate :Int
        get() = pref.getInt("APP_ACCESS_DATE",0)
        set(value) = pref.edit().putInt("APP_ACCESS_DATE",value).apply()


    var userPermission : String
        get() = pref.getString(PreferenceConstants.USER_PERMISSION, "")!!
        set(v){
            pref.edit().putString(PreferenceConstants.USER_PERMISSION, v).apply()
        }

    var day :Int
        get() = pref.getInt("day",0)
        set(v){
            pref.edit().putInt("day", v).apply()
        }



    fun addArticle(article: Article) {
        val articles = readFromSharedPreferences()

        articles.add(article)

        saveToSharedPreferences(articles)
    }
    fun saveToSharedPreferences(articles: MutableList<Article>){
        val editor = pref.edit()

        // Article 리스트를 JSON으로 변환하여 저장
        val json = gson.toJson(articles)
        editor.putString(PreferenceConstants.BOOK_MAKER, json)
        editor.apply()
    }

    fun readFromSharedPreferences(): MutableList<Article> {
        // 저장된 JSON 문자열을 가져와서 Article 리스트로 변환
        val bookmark = pref.getString(PreferenceConstants.BOOK_MAKER, "") ?: ""
        val listType = object : TypeToken<MutableList<ArticleEntity>>() {}.type
        return gson.fromJson(bookmark, listType) ?: mutableListOf<Article>()
    }

}