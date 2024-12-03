package com.devlog.notification

import android.content.Context
import android.content.Intent
import com.devlog.main.MainActivity

class AppNotificationIntentProvider : NotificationIntentProvider {
    override fun getNotificationIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_NOTIFICATION_CLICKED", true)
        }
    }
}