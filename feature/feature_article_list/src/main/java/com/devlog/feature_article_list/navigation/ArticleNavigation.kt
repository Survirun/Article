package com.devlog.feature_article_list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.feature_article_list.ArticleSeen
import com.devlog.feature_article_list.state.ArticleTabState

fun NavController.navigateArticle() {
    navigate(articleRoute.route) {
        popUpTo(0) { inclusive = true }
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
    provideArticleArray: () -> ArrayList<ArticleTabState>,
    onComplete: (title:String,url:String) -> Unit
) {
    composable(route = articleRoute.route ,) {
        val articleArrayState = provideArticleArray()
       ArticleSeen(articles = articleArrayState , onComplete)
    }
}

object articleRoute {
    const val route = "article"
}