package com.devlog.article.presentation.my_keywords_select

import android.os.ResultReceiver
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.main.MainRoute
import com.devlog.article.presentation.splash.SplashScreen2


fun NavController.myKeywordSelectNavigationCompensation() {
    navigate(MyKeywordSelectNCompensation.route) {
        launchSingleTop = true
        popUpTo(MyKeywordSelectNCompensation.route) { inclusive = true }
    }
}

fun NavGraphBuilder.myKeywordSelectNavGraph(onComplete: () -> Unit) {
    composable(route = MyKeywordSelectNCompensation.route) {
        MyKeywordSelectSeen(onComplete = onComplete)
    }
}

object MyKeywordSelectNCompensation {
    const val route = "myKeywordSelect"
}