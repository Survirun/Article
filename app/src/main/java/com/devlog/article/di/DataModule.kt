package com.devlog.article.di

import com.devlog.article.data.repository.v3.ArticleRepository
import com.devlog.article.data.repository.v3.ArticleRepositoryImpl
import com.devlog.article.data.repository.v3.UserRepository
import com.devlog.article.data.repository.v3.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsTopicRepository(
        topicsRepository: ArticleRepositoryImpl,
    ): ArticleRepository

    @Binds
    abstract fun bindsUserRepository3(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository


}
