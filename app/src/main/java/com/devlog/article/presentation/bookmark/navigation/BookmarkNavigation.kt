package com.devlog.article.presentation.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.bookmark.BookmarkSeen

fun NavController.navigateBookmark(){
    navigate(SignInRoute.route)
}

fun NavGraphBuilder.bookmarkNavGraph(  onComplete: (title:String,url:String) -> Unit) {

    composable(route = SignInRoute.route) {
        BookmarkSeen(onComplete = onComplete)

    }
}

object SignInRoute{
    const val  route ="book_mark_route"

}