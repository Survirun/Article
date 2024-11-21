package com.devlog.date.entity.question

import com.devlog.model.data.entity.question.Question

data class Day(
    val day: Int,
    val title:String,
    val questions: List<Question>
)
