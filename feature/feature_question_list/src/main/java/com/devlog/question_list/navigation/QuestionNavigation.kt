package com.devlog.question_list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.model.data.entity.response.quiz.Quiz
import com.devlog.question_list.QuestionSeen

fun NavController.navigateQuestion() {
    navigate(QuestionRoute.route){
        popUpTo(0) // 'home' 화면으로 돌아가도 스택을 초기화
        launchSingleTop = true // 이미 'home'에 있으면 새 항목을 추가하지 않음
    }
}

fun NavGraphBuilder.questionNavGraph(
    onQuestionClick: (quiz:Quiz) -> Unit
) {
    composable(route = QuestionRoute.route) {
        QuestionSeen(onQuestionClick =onQuestionClick)
    }
}

object QuestionRoute {
    const val route = "question"
}