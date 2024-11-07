package com.devlog.article.presentation.splash

import android.os.ResultReceiver
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.question_compensation.QuestionCompensationSeen


fun NavController.splashNavigationCompensation() {
    navigate(SplashNCompensation.route)
}

fun NavGraphBuilder.splashNavGraph(resultReceiver : ResultReceiver) {
    composable(route = SplashNCompensation.route) {
        SplashScreen2(resultReceiver = resultReceiver)
    }
}

object SplashNCompensation {
    const val route = "splash"
}