package com.devlog.question_list.intent

sealed class QuestionIntent {
    data object Initialize : QuestionIntent()
    data object  GetQuestion : QuestionIntent()
}