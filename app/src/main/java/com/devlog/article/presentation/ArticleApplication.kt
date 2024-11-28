package com.devlog.article.presentation

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.utility.NotificationWorker
import com.devlog.article.utility.UtilManager.getTodayToInt
import com.devlog.data.repository.v3.ArticleRepository2
import com.devlog.feature_app_widget_provider.AppWidgetProviderArticle
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ArticleApplication : Application(), Configuration.Provider  {

    companion object {
        const val TAG = "Application"

        @Volatile
        lateinit var instance: ArticleApplication


    }

    init {
        instance = this


    }
    @Inject
    lateinit var apiRepository: ArticleRepository2
    @Inject
    lateinit var workerFactory: HiltWorkerFactory



    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()




    override fun onCreate() {
        super.onCreate()
        PrefManager.init(applicationContext)
        PrefManager.appAccessDate = getTodayToInt()
        com.devlog.preference.PrefManager.init(applicationContext)
        com.devlog.preference.PrefManager.appAccessDate = getTodayToInt()

        MixPanelManager.init(applicationContext)
        createWorkRequest()

        //updateWidget()
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "FCM 토큰 가져오기 실패", task.exception)
                return@addOnCompleteListener
            }

            // 토큰 가져오기 성공
            val token = task.result
            Log.d("FCM", "FCM 토큰: $token")
        }
    }

    private fun createWorkRequest() {

        val times = listOf(8,10,12, 14, 16, 17,18,19,20,21, 22, 23) // 12시, 4시, 6시, 9시 ,11시
        val titles = listOf(
            "최신 아티클이 도착했습니다! 놓치지 마세요!",
            "최신 아티클이 도착했습니다! 놓치지 마세요!",
            "긴급! 당신이 놓친 아티클이 여기에!",
            "긴급! 당신이 놓친 아티클이 여기에!",
            "긴급! 당신이 놓친 아티클이 여기에!",
            "당신의 동료가 읽고 있는 그 글, 왜 당신은 아직 모르나요?",
            "당신의 동료가 읽고 있는 그 글, 왜 당신은 아직 모르나요?",
            "당신의 동료가 읽고 있는 그 글, 왜 당신은 아직 모르나요?",
            "모두가 읽은 아티클, 당신은 아직도 안 읽었나요?",
            "모두가 읽은 아티클, 당신은 아직도 안 읽었나요?",
            "모두가 읽은 아티클, 당신은 아직도 안 읽었나요?",
            "띵동! 당신의 택배가 도착했습니다!"
        )
        val subtitles = listOf(
            "점심시간에 조금의 여유를 가지세요. 이 이야기를 놓치면, 당신의 하루가 평범하게 지나가고 중요한 통찰을 놓칠 수 있습니다. 클릭해 보세요.",
            "점심시간에 조금의 여유를 가지세요. 이 이야기를 놓치면, 당신의 하루가 평범하게 지나가고 중요한 통찰을 놓칠 수 있습니다. 클릭해 보세요.",
            "잘 해결안되는 일이 있나요? 다른 회사들에서 문제를 해결하는법 찾아보기",
            "잘 해결안되는 일이 있나요? 다른 회사들에서 문제를 해결하는법 찾아보기",
            "잘 해결안되는 일이 있나요? 다른 회사들에서 문제를 해결하는법 찾아보기",
            "잘 해결안되는 일이 있나요? 다른 회사들에서 문제를 해결하는법 찾아보기",
            "내일 동료들과의 대화에서 놓칠 수 있는 주제일지도 모릅니다. 이 글을 읽지 않으면, 중요한 대화에서 뒤처질 수 있습니다.",
            "내일 동료들과의 대화에서 놓칠 수 있는 주제일지도 모릅니다. 이 글을 읽지 않으면, 중요한 대화에서 뒤처질 수 있습니다.",
            "내일 동료들과의 대화에서 놓칠 수 있는 주제일지도 모릅니다. 이 글을 읽지 않으면, 중요한 대화에서 뒤처질 수 있습니다.",
            "내일 동료들과의 대화에서 놓칠 수 있는 주제일지도 모릅니다. 이 글을 읽지 않으면, 중요한 대화에서 뒤처질 수 있습니다.",
            "이 글을 읽지 않으면, 내일의 이야기에 뒤처질 수 있습니다. 지금 바로 확인해 보세요!",
            "내일 회의에서 필요한 정보 다 여기 있어요."
        )

        for (i in times.indices) {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()

            dueDate.set(Calendar.HOUR_OF_DAY, times[i])
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)

            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }

            val delayTime = dueDate.timeInMillis - currentDate.timeInMillis

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false) // 배터리 상태에 상관없이 실행
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // 네트워크 필요 없음
                .build()

            val data = Data.Builder()
                .putString("title", titles[i])
                .putString("subtitle", subtitles[i])
                .putInt("time", times[i])
                .build()


            val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delayTime, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setInputData(data)
                .build()


            WorkManager.getInstance(this).enqueue(dailyWorkRequest)


        }

    }

    private fun updateWidget() {
        val context = this
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, AppWidgetProviderArticle::class.java)

        // 위젯 ID들을 가져옴
        val appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)

        // 위젯 업데이트 브로드캐스트 보내기
        val intent = Intent(context, AppWidgetProviderArticle::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        context.sendBroadcast(intent)
    }




}