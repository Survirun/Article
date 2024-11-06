package com.devlog.article.presentation.article_v2

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.article.ArticleTabState
import com.devlog.article.presentation.question.QuestionSeen

fun NavController.navigateArticle() {
    navigate(articleRoute.route)
}

fun NavGraphBuilder.articleNavGraph(
    articles:ArrayList<ArticleTabState>
) {
    composable(route = articleRoute.route) {
       ArticleSeen(articles =articles )
    }
}

object articleRoute {
    const val route = "article"
}