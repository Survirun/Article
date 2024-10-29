package com.devlog.article.di

import android.content.Context
import com.devlog.article.data.entity.question.Week
import com.devlog.article.data.repository.QuestionRepository
import com.devlog.article.data.repository.QuestionRepositoryImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // 주차 데이터를 JSON에서 로드하여 제공하는 함수
    @Provides
    @Singleton
    fun provideWeeks(@ApplicationContext context: Context): List<Week> {
        val json = context.assets.open("weekData.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Week>>() {}.type
        return Gson().fromJson(json, type)
    }

    // QuestionRepository를 생성하여 Hilt에 제공
    @Provides
    @Singleton
    fun provideQuestionRepository(weeks: List<Week>): QuestionRepository {
        return QuestionRepositoryImpl(weeks)
    }
}