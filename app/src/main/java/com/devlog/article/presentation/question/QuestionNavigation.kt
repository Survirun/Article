package com.devlog.article.presentation.question

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavController.navigateQuestion() {
    navigate(QuestionRoute.route)
}

fun NavGraphBuilder.questionNavGraph(
    onQuestionClick: () -> Unit
) {
    composable(route = QuestionRoute.route) {
        QuestionSeen(onQuestionClick =onQuestionClick)
    }
}

object QuestionRoute {
    const val route = "question"
}