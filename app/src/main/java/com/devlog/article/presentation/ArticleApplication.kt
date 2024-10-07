package com.devlog.article.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.utility.NotificationWorker
import com.devlog.article.utility.UtilManager.getTodayToInt
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class ArticleApplication : Application() {

    companion object {
        const val TAG = "Application"

        @Volatile
        lateinit var instance: ArticleApplication


    }
    init {
        instance =this




    }
    override fun onCreate() {
        super.onCreate()
        MixPanelManager.init(applicationContext)
        PrefManager.init(applicationContext)
        PrefManager.appAccessDate = getTodayToInt()
        createWorkRequest()

    }

    private fun createWorkRequest(){

        val times = listOf(12, 16, 18, 21,23) // 12시, 4시, 6시, 9시 ,11시
        val titles = listOf("Title 12PM", "Title 4PM", "Title 6PM", "Title 9PM")
        val subtitles = listOf("Subtitle 12PM", "Subtitle 4PM", "Subtitle 6PM", "Subtitle 9PM")

        for (i in times.indices) {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()

            dueDate.set(Calendar.HOUR_OF_DAY, times[i])
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)

            if(dueDate.before(currentDate)) {
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
                .putInt("time",times[i])
                .build()


            val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delayTime, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setInputData(data)
                .build()


            WorkManager.getInstance(this).enqueue(dailyWorkRequest)


        }

    }



}