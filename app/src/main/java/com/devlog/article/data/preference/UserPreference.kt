package com.devlog.article.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class UserPreference private constructor(context: Context) {
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


    var coin :Int
        get() = userPreference.getInt("coin", 0)
        set(v) {
            userPreferenceEditor.putInt("coin", v)
            userPreferenceEditor.apply()
        }

    var userInventory :String
        get() = userPreference.getString("userInventory", "").toString()
        set(v) {
            userPreferenceEditor.putString("userInventory", v)
            userPreferenceEditor.apply()
        }

    var userChair:Int
        get() = userPreference.getInt("userChair", -1)
        set(v) {
            userPreferenceEditor.putInt("userChair", v)
            userPreferenceEditor.apply()
        }

    var userDesk:Int
        get() = userPreference.getInt("userDesk", -1)
        set(v) {
            userPreferenceEditor.putInt("userDesk", v)
            userPreferenceEditor.apply()
        }
    var userComputer:Int
        get() = userPreference.getInt("userComputer", -1)
        set(v) {
            userPreferenceEditor.putInt("userComputer", v)
            userPreferenceEditor.apply()
        }
    var userSubItem:Int
        get() = userPreference.getInt("userSubItem", -1)
        set(v) {
            userPreferenceEditor.putInt("userSubItem", v)
            userPreferenceEditor.apply()
        }
}