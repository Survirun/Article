package com.devlog.feature_app_widget_provider

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devlog.data.repository.v3.ArticleRepository2
import com.devlog.model.data.entity.response.Article
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnFailure
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@HiltWorker
class WidgetUpdateWorker  @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,


)  : CoroutineWorker(context, workerParams){
    @Inject
    lateinit var apiRepository: ArticleRepository2

    override suspend fun doWork(): Result {
        // ViewModel에서 데이터를 가져옴

        // LiveData의 값을 관찰하고 데이터 업데이트
        val entryPoint = EntryPointAccessors.fromApplication(applicationContext, MyWidgetEntryPoint::class.java)

        val response = entryPoint.getMyRepository().getArticle(1)
        Log.d("polaris","시작")
        if (response != null) {
            response.suspendMapSuccess {
                Log.d("polaris","성공")
                updateWidget(context = applicationContext,data.articles)
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

    private fun updateWidget(context: Context,data: List<Article>) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, AppWidgetProviderArticle::class.java))
        val intent = Intent(context, RemoteViewsService::class.java)
        // 데이터를 MyRemoteViewsFactory로 전달
        Log.d("polaris","updateWidget")
        MyRemoteViewsFactory.updateData(data,intent)
        Log.d("polaris","updateWidget")
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_provider_layout)

            // RemoteViewsService를 설정하여 데이터를 표시

            views.setRemoteAdapter(R.id.widget_list_view, intent)

            // 위젯 업데이트
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}