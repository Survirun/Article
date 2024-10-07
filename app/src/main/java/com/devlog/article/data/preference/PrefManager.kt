package com.devlog.article.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


@SuppressLint("StaticFieldLeak")
object PrefManager {
    private lateinit var context: Context
    private lateinit var pref: SharedPreferences


    private const val KEY_INIT_PREF_MANAGER = "KEY_INIT_PREF_MANAGER"

    fun init(mContext: Context) {
        context = mContext
        pref = context.getSharedPreferences(KEY_INIT_PREF_MANAGER, Context.MODE_PRIVATE)
    }

    //앱 마지막 접속 날자
    var appAccessDate :Int
        get() = pref.getInt("APP_ACCESS_DATE",0)
        set(value) = pref.edit().putInt("APP_ACCESS_DATE",value).apply()



}