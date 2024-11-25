package com.devlog.article.di

import com.devlog.article.data.repository.v3.ArticleRepository3
import com.devlog.article.data.repository.v3.OfflineFirstTopicsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsTopicRepository(
        topicsRepository: OfflineFirstTopicsRepository,
    ): ArticleRepository3


}
