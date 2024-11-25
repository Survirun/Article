package com.devlog.feature_splash.navigation

import android.os.ResultReceiver
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.feature_splash.SplashScreen


fun NavController.splashNavigationCompensation(mainRoute: String) {
    navigate(SplashNCompensation.route) {
        launchSingleTop = true
        popUpTo(mainRoute) { inclusive = true }
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