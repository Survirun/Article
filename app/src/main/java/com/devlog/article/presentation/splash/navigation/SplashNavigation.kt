package com.devlog.article.presentation.splash.navigation

import android.os.ResultReceiver
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.main.MainRoute
import com.devlog.article.presentation.splash.SplashScreen2


fun NavController.splashNavigationCompensation() {
    navigate(SplashNCompensation.route) {
        launchSingleTop = true
        popUpTo(MainRoute.route) { inclusive = true }
    }
}

fun NavGraphBuilder.splashNavGraph(resultReceiver : ResultReceiver,onComplete: () -> Unit) {
    composable(route = SplashNCompensation.route) {
        SplashScreen2(resultReceiver = resultReceiver, onComplete = onComplete)
    }
}

object SplashNCompensation {
    const val route = "splash"
}