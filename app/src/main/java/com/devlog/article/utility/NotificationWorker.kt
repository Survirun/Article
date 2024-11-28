package com.devlog.article.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devlog.article.R
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.utility.UtilManager.getTodayToInt
import com.devlog.main.MainActivity


class NotificationWorker(var context: Context, params: WorkerParameters) : Worker(context, params) {

    // 실제 작업을 수행하는 doWork() 메서드
    override fun doWork(): Result {
        // 알림을 보내는 메서드 호출
        val title = inputData.getString("title") ?: "Default Title"
        val subtitle = inputData.getString("subtitle") ?: "Default Subtitle"
        val time = inputData.getInt("time", 0)
        if (time > 12) {
            if (PrefManager.appAccessDate == getTodayToInt()) {
                return Result.success()
            }else{
                showNotification(title, subtitle)
                return Result.success()
            }
        }
        showNotification(title, subtitle)

        return Result.success()  // 작업 성공 반환
    }

    // 알림을 보내는 메서드
    fun showNotification(title: String, subtitle: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_notification_channel"

        // Android 8.0 이상에서 NotificationChannel을 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = subtitle
            }
            notificationManager.createNotificationChannel(channel)  // 채널 생성
        }

        // 알림을 눌렀을 때 열릴 인텐트 생성
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_NOTIFICATION_CLICKED", true)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // 알림 생성
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)  // 알림 제목 설정
            .setContentText(subtitle)  // 알림 메시지 설정
            .setSmallIcon(R.drawable.ic_launcher_app_logo_foreground)  // 알림 아이콘 설정
            .setContentIntent(pendingIntent)  // 인텐트 설정
            //.setColor(ContextCompat.getColor(context, R.color.color_all_main_color))
            .setAutoCancel(true)  // 알림을 클릭하면 자동으로 삭제
            .build()


        notificationManager.notify(1, notification)
        //MixPanelManager.receiveApptechBeforeSleepNoti()


    }
}