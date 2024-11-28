package com.devlog.article

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devlog.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService() : FirebaseMessagingService() {
    var url =""
    var title = ""
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "From: ${remoteMessage.from}")


        remoteMessage.data.let {
            url = it["url"]?:""
            title = it["title"]?:""
        }

        // 메시지에 알림이 포함된 경우 처리
        remoteMessage.notification?.let { it ->
            Log.d("FCM", "Message Notification Title: ${it.title}")
            Log.d("FCM", "Message Notification Body: ${it.body}")

            Log.d("FCM", "Message Notification Title: ${url}")
            Log.d("FCM", "Message Notification Title: ${title}")

            // 알림을 보냄
            sendNotification(it.title ?: "Default Title", it.body ?: "Default Body")
        }
    }

    // 알림을 생성하는 함수
    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("url", url)
            putExtra("title",title)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        }


        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 채널 생성 (Android 8.0 이상)
        val channelId = "default_channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "FCM Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "FCM Channel Description"
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 설정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_app_logo_foreground)  // 알림 아이콘
            .setContentTitle(title)  // 알림 제목
            .setContentText(messageBody)  // 알림 내용
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 알림 우선순위
            .setContentIntent(pendingIntent)  // 알림 클릭 시 실행할 인텐트
            .setAutoCancel(true)  // 클릭 시 알림 자동 삭제

        // 알림을 시스템에 표시
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MyFirebaseMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(0, notificationBuilder.build())
        }
    }
}