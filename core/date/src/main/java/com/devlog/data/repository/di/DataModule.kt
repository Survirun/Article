package com.devlog.data.repository.di

import com.devlog.article.data.repository.v3.UserRepository2
import com.devlog.data.repository.v3.ArticleRepository2
import com.devlog.data.repository.v3.ArticleRepositoryImpl
import com.devlog.data.repository.v3.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsTopicRepository2(
        topicsRepository: ArticleRepositoryImpl,
    ): ArticleRepository2

    @Binds
    abstract fun bindsUserRepository2(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository2


}
