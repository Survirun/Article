package com.devlog.article.data.entity.question

data class Question(
    val id: String,
    val category: String,
    val question: String,
    val options: List<String>,
    val answer: String,
    val explanation: String
)