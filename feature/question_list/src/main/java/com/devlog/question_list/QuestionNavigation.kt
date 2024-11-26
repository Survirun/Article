package com.devlog.question_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavController.navigateQuestion() {
    navigate(QuestionRoute.route){
        popUpTo(0) // 'home' 화면으로 돌아가도 스택을 초기화
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