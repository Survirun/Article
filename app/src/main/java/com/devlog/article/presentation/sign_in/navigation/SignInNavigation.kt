package com.devlog.article.presentation.sign_in.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.devlog.article.presentation.main.MainRoute
import com.devlog.article.presentation.sign_in.SignInSeen

fun NavController.navigateSignIn(){
    navigate(SignInRoute.route)
}

fun NavGraphBuilder.signInNavGraph(login:()->Unit) {

    composable(route = SignInRoute.route) {
        SignInSeen(login)

    }
}

object SignInRoute{
    const val  route ="sign_in_route"

}