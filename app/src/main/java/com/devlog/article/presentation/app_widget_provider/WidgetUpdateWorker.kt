package com.devlog.article.presentation.app_widget_provider

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devlog.article.R
import com.devlog.article.data.entity.article.Passed

import com.devlog.article.data.response.Article
import com.devlog.article.presentation.ArticleApplication
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnFailure
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WidgetUpdateWorker  @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,

) : CoroutineWorker(context, workerParams){

    override suspend fun doWork(): Result {
        // ViewModel에서 데이터를 가져옴

        // LiveData의 값을 관찰하고 데이터 업데이트
        val response =

        ArticleApplication.instance.apiRepository.getArticle(1, Passed(arrayListOf()))
        Log.d("polaris","시작")
        if (response != null) {
            response.suspendMapSuccess {
                Log.d("polaris","성공")
                updateWidget(data.articles)
            }
            response.suspendOnError {
                Log.d("polaris","실패")
            }
            response.suspendOnFailure {
                Log.d("polaris","실패")
            }


        }

        return Result.success()
    }

    private fun updateWidget(data: List<Article>) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, AppWidgetProviderArticle::class.java))
        val intent = Intent(applicationContext, RemoteViewsService::class.java)
        // 데이터를 MyRemoteViewsFactory로 전달
        MyRemoteViewsFactory.updateData(data,intent)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(applicationContext.packageName, R.layout.widget_provider_layout)

            // RemoteViewsService를 설정하여 데이터를 표시

            views.setRemoteAdapter(R.id.widget_list_view, intent)

            // 위젯 업데이트
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}