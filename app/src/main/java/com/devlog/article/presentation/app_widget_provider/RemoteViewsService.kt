package com.devlog.article.presentation.app_widget_provider

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.devlog.article.R

class RemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?):RemoteViewsFactory {
        return MyRemoteViewsFactory(this.applicationContext)
    }
}

class MyRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val dataList: MutableList<String> = mutableListOf()

    // 초기화
    override fun onCreate() {
        // 데이터를 초기화하거나 불러오기 (여기서는 간단한 데이터 예시)
        dataList.addAll(listOf("Item 1", "Item 2", "Item 3", "Item 4","Item 1", "Item 2", "Item 3", "Item 4","Item 1", "Item 2", "Item 3", "Item 4","Item 1", "Item 2", "Item 3", "Item 4","Item 1", "Item 2", "Item 3", "Item 4","Item 1", "Item 2", "Item 3", "Item 4"))
    }

    // 데이터 갱신
    override fun onDataSetChanged() {
        // 필요에 따라 데이터를 새로 고칠 수 있음 (API 호출 등)
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_list_item)
        views.setTextViewText(R.id.widget_item_text, dataList[position])
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun onDestroy() {}
}