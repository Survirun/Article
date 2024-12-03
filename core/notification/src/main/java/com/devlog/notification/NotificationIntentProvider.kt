package com.devlog.notification

import android.content.Context
import android.content.Intent

interface NotificationIntentProvider {
    fun getNotificationIntent(context: Context): Intent
}