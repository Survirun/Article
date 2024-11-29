package com.devlog.model.data.entity.response.quiz

import java.io.Serializable

data class Question (
    val qid: String,
    val category: String,
    val question: String,
    val options: List<String>,
    val answer: String,
    val explanation: String
): Serializable