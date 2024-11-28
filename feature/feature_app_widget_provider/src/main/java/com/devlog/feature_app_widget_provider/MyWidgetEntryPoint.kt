package com.devlog.feature_app_widget_provider

import com.devlog.data.repository.v3.ArticleRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MyWidgetEntryPoint {
    fun getMyRepository(): ArticleRepository
}