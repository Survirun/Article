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

}