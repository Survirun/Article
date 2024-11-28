package com.devlog.feature_book_mark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.feature_book_mark.BookmarkSeen

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