package com.devlog.feature_app_widget_provider

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.devlog.model.data.entity.response.Article
import java.util.UUID

class RemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return MyRemoteViewsFactory(this.applicationContext, intent!!)
    }
}

class MyRemoteViewsFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    companion object {
        private val dataList: MutableList<Article> = mutableListOf()

        // 외부에서 데이터 업데이트
        fun updateData(newData: List<Article>, intent: Intent) {
            dataList.clear()
            dataList.addAll(newData.subList(0, newData.size/2))
        }
    }

    private val appWidgetId: Int = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    // 초기화
    override fun onCreate() {
        // 데이터를 초기화하거나 불러오기 (여기서는 간단한 데이터 예시)
    }

    // 데이터 갱신
    override fun onDataSetChanged() {
        // 필요에 따라 데이터를 새로 고칠 수 있음 (API 호출 등)
    }

    override fun getCount(): Int {
        Log.d("polaris","getCount${dataList.size}")
        return dataList.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d("polaris","getViewAt")
        val views = RemoteViews(context.packageName, R.layout.widget_list_item)
        views.setTextViewText(R.id.widget_item_title_text, dataList[position].title)
        views.setTextViewText(R.id.widget_item_sub_title_text, dataList[position].snippet ?: "")

        val imageUrl = dataList[position].thumbnail
        if (!imageUrl.isNullOrBlank()) {
            try {
                // 이미지 동기 로드
                val bitmap = Glide.with(context.applicationContext)
                    .asBitmap()
                    .load(imageUrl)
                    .submit() // 동기적으로 이미지 로드
                    .get()

                // 이미지를 성공적으로 로드하면 setImageViewBitmap으로 설정
                views.setImageViewBitmap(R.id.imageView1, bitmap)
            } catch (e: Exception) {
                Log.e("GlideError", "Failed to load image: $imageUrl", e)
            }
        }else{
            views.setImageViewBitmap(R.id.imageView1, null)
        }

        // 클릭 이벤트를 처리하려면 PendingIntent를 설정
        val fillInIntent = Intent().apply {
            action = UUID.randomUUID().toString() // 고유한 Action 설정
            putExtra("title", dataList[position].title)
            putExtra("url", dataList[position].link)
        }

        views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent)
        Log.d("IntentData", "URL: ${fillInIntent.getStringExtra("url")}")
       // views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent)

        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun onDestroy() {}
}
