package com.devlog.article.presentation.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.feature_splash.navigation.SplashNCompensation


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