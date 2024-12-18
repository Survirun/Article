package com.devlog.article.data.mixpanel

import android.content.Context
import com.devlog.preference.PrefManager

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

class Mixpanel(context: Context) {

    val mixpanel = MixpanelAPI.getInstance(context, "3ea37124de3c3eafaa11902507c88b14", true).apply {
        identify(distinctId, true)

        val args = mapOf<String, Any>(
            "User Name" to PrefManager.userName,
        )
        val props = JSONObject(args)
        people.set(props)
    }

    fun mixpanelEvent(name: String) {

        mixpanel.track(name)

    }

    fun mixpanelEvent(name: String, args: Map<String, Any>) {
        val props = JSONObject(args)
        mixpanel.track(name, props)

    }


}