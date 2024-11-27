package com.devlog.feature_my_keywords_select.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.feature_my_keywords_select.MyKeywordSelectSeen


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