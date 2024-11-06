package com.devlog.article.presentation.question

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.main.MainRoute

fun NavController.navigateQuestion() {
    navigate(QuestionRoute.route){
        popUpTo(MainRoute.route) { inclusive = true } // 'home' 화면으로 돌아가도 스택을 초기화
        launchSingleTop = true // 이미 'home'에 있으면 새 항목을 추가하지 않음
    }
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