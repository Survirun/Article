package com.devlog.article.presentation.article.navigation

import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.article.ArticleSeen
import com.devlog.article.presentation.main.MainRoute
import com.devlog.article.presentation.main.MainViewModel
import com.devlog.article.presentation.splash.navigation.SplashNCompensation

fun NavController.navigateArticle() {
    navigate(articleRoute.route) {
        popUpTo(SplashNCompensation.route) { inclusive = true }
        launchSingleTop = true
    }
//    navigate(articleRoute.route){
//        popUpTo(MainRoute.route) { inclusive = true } // 'home' 화면으로 돌아가도 스택을 초기화
//        launchSingleTop = true // 이미 'home'에 있으면 새 항목을 추가하지 않음
//    }
//    navigate(articleRoute.route) {
//        popUpTo(SplashNCompensation.route) { inclusive = true }
//        launchSingleTop = true
//    }
}

fun NavGraphBuilder.articleNavGraph(
   viewModel:MainViewModel
) {
    composable(route = articleRoute.route) {
        val articleArrayState by viewModel.articleArray
       ArticleSeen(articles =articleArrayState )
    }
}

object articleRoute {
    const val route = "article"
}