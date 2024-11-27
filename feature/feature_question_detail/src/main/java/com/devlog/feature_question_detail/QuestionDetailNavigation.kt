package com.devlog.feature_question_detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavController.navigateQuestionDetail() {
    navigate(QuestionDetailRoute.route)
}

fun NavGraphBuilder.questionDetailNavGraph(onQuestionComplete: () -> Unit) {
    composable(route = QuestionDetailRoute.route) {
        QuestionDetailSeen(onQuestionComplete = onQuestionComplete)
    }
}

object QuestionDetailRoute {
    const val route = "question_detail"
}