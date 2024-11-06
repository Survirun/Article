package com.devlog.article.presentation.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devlog.article.presentation.question_detail.QuestionDetailSeen


fun NavController.navigateMain() {
    navigate(MainRoute.route)
}



object MainRoute {
    const val route = "main"
}