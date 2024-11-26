package com.devlog.feature_question_compensation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavController.navigateQuestionCompensation() {
    navigate(QuestionCompensation.route)
}

fun NavGraphBuilder.questionCompensationNavGraph(onComplete: () -> Unit) {
    composable(route = QuestionCompensation.route) {
        QuestionCompensationSeen(onComplete = onComplete)
    }
}

object QuestionCompensation {
    const val route = "question_compensation"
}