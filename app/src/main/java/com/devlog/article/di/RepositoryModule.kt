package com.devlog.article.di

import com.devlog.article.data.repository.v2.question.QuestionRepository
import com.devlog.article.data.repository.v2.question.QuestionRepositoryImpl
import com.devlog.date.entity.question.Week
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {



    // QuestionRepository를 생성하여 Hilt에 제공
    @Provides
    @Singleton
    fun provideQuestionRepository(weeks: List<Week>): QuestionRepository {
        return QuestionRepositoryImpl(weeks)
    }
}