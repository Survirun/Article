package com.devlog.article.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class UserPreference(context: Context) {
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

}