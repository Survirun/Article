package com.devlog.article.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserPreference private constructor(private var context: Context) {
    companion object {

        // Instance 생성
        @SuppressLint("StaticFieldLeak")
        private var instance: UserPreference? = null
        fun getInstance(context: Context): UserPreference {
            if (instance == null) instance = UserPreference(context)
            return instance as UserPreference
        }
    }
    private val userPreference: SharedPreferences =
        context.getSharedPreferences(PreferenceConstants.USERP_PREFETANCE, Context.MODE_PRIVATE)
    private val userPreferenceEditor: SharedPreferences.Editor = userPreference.edit()

    var userSignInCheck: Boolean
        get() = userPreference.getBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, false)
        set(v) {
            userPreferenceEditor.putBoolean(PreferenceConstants.USER_SIGN_IN_CHECK, v)
            userPreferenceEditor.apply()
        }

    var userKeywordCheck :Boolean
        get() = userPreference.getBoolean(PreferenceConstants.USER_KEYWORD_CHECK, false)
        set(v) {
            userPreferenceEditor.putBoolean(PreferenceConstants.USER_KEYWORD_CHECK, v)
            userPreferenceEditor.apply()
        }
    var userUid:String
        get() = userPreference.getString(PreferenceConstants.USER_UID, "").toString()
        set(v) {
            userPreferenceEditor.putString(PreferenceConstants.USER_UID, v)
            userPreferenceEditor.apply()
        }
    var userName: String
        get() = userPreference.getString(PreferenceConstants.USER_NAME, "")!!
        set(v) {
            userPreferenceEditor.putString(PreferenceConstants.USER_NAME, v)
            userPreferenceEditor.apply()
        }

    var userAge :String
        get() = userPreference.getString(PreferenceConstants.USER_AGE, "")!!
        set(v) {
            userPreferenceEditor.putString(PreferenceConstants.USER_AGE, v)
            userPreferenceEditor.apply()
        }

    var userAccessToken : String
        get() = userPreference.getString(PreferenceConstants.USER_ACCESS_TOKEN, "")!!
        set(v){
            userPreferenceEditor.putString(PreferenceConstants.USER_ACCESS_TOKEN, v)
            userPreferenceEditor.apply()
        }
    var userPermission : String
        get() = userPreference.getString(PreferenceConstants.USER_PERMISSION, "")!!
        set(v){
            userPreferenceEditor.putString(PreferenceConstants.USER_PERMISSION, v)
            userPreferenceEditor.apply()
        }
    fun setUserPagePassed(value: ArrayList<String>?) {
        val json = Gson().toJson(value)
        userPreferenceEditor.putString("Pages_passed_user", json).apply()
    }


    fun getUserPagePassed(): ArrayList<String> {
        val json = userPreference.getString("Pages_passed_user", null)
        return Gson().fromJson(json, object: TypeToken<ArrayList<String>>() {}.type) ?: arrayListOf<String>()
    }

}