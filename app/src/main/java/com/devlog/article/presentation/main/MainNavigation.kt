package com.devlog.article.presentation.main

import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devlog.article.presentation.article_v2.ArticleSeen
import com.devlog.article.presentation.article_v2.articleRoute
import com.devlog.article.presentation.question_detail.QuestionDetailSeen
import com.devlog.article.presentation.splash.SplashNCompensation


fun NavController.navigateMain() {
    navigate(MainRoute.route) {
        popUpTo(SplashNCompensation.route) { inclusive = true }
        launchSingleTop = true
    }

}
fun NavGraphBuilder.aminNavGraph() {

    composable(route = MainRoute.route) {

    }
}


object MainRoute {
    const val route = "main"
}