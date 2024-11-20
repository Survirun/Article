package com.devlog.article.presentation.splash.navigation

import android.os.ResultReceiver
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.main.navigation.MainRoute
import com.devlog.article.presentation.splash.SplashScreen


fun NavController.splashNavigationCompensation() {
    navigate(SplashNCompensation.route) {
        launchSingleTop = true
        popUpTo(MainRoute.route) { inclusive = true }
    }
}

fun NavGraphBuilder.splashNavGraph(resultReceiver : ResultReceiver, loginCheck:()->Unit,keywordCheck:()->Unit) {
    composable(route = SplashNCompensation.route) {
        SplashScreen(resultReceiver = resultReceiver, loginCheck = loginCheck , keywordCheck = keywordCheck)
    }
}

object SplashNCompensation {
    const val route = "splash"
}