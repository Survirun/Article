package com.devlog.data.repository.di

import android.content.Context
import android.util.Log
import com.devlog.data.repository.v2.question.QuestionRepository2
import com.devlog.data.repository.v2.question.QuestionRepositoryImpl2
import com.devlog.date.entity.question.Week
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
object RepositoryModule2 {

    // 주차 데이터를 JSON에서 로드하여 제공하는 함수
    @Provides
    @Singleton
    fun provideWeeks2(@ApplicationContext context: Context): List<Week> {
        // JSON 파일을 읽어 옵니다.
        val json = context.assets.open("output.json").bufferedReader().use { it.readText() }

        // TypeToken을 사용하여 List<Week> 타입으로 파싱합니다.
        val type = object : TypeToken<List<Week>>() {}.type
        val weeks: List<Week> = Gson().fromJson(json, type)

        // 변환된 List<Week> 객체를 JSON 문자열로 출력하여 디버그 확인
        Log.d("polaris2", Gson().toJson(weeks))

        return weeks
    }

    // QuestionRepository를 생성하여 Hilt에 제공
    @Provides
    @Singleton
    fun provideQuestionRepository2(weeks: List<Week>): QuestionRepository2 {
        return QuestionRepositoryImpl2(weeks)
    }
}