package com.devlog.article.presentation.question_compensation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.question_detail.QuestionDetailSeen

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