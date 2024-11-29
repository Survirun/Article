package com.devlog.model.data.entity.response.quiz

import java.io.Serializable

data class Quiz(
    val title: String,
    val day: Int,
    val questions: List<Question>
): Serializable