package com.devlog.data.repository.di

import com.devlog.data.repository.v2.question.QuestionRepository2
import com.devlog.data.repository.v2.question.QuestionRepositoryImpl2
import com.devlog.data.repository.v3.ArticleRepository
import com.devlog.data.repository.v3.ArticleRepositoryImpl
import com.devlog.data.repository.v3.UserRepository
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
    ): ArticleRepository

    @Binds
    abstract fun bindsUserRepository2(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository


    @Binds
    abstract fun bindsQuestionRepositoryImpl2(
        userRepositoryImpl: QuestionRepositoryImpl2,
    ): QuestionRepository2


}
