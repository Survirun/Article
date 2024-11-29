package com.devlog.feature_question_detail

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devlog.model.data.entity.response.quiz.Quiz
import com.devlog.util.UtilManager.toJson
import com.google.gson.Gson


fun NavController.navigateQuestionDetail(quiz: Quiz) {
    navigate("${QuestionDetailRoute.route}/${Uri.encode(Gson().toJson(quiz))}")
}

fun NavGraphBuilder.questionDetailNavGraph(onQuestionComplete: () -> Unit) {
    composable(route = "${QuestionDetailRoute.route}/{test}",arguments=  listOf(navArgument("test") { type = NavType.StringType })) { navBackStackEntry->

        val teamDataJson = navBackStackEntry.arguments?.getString("test")
        Log.d("polaris2",teamDataJson!!.toJson())
        val teamData = Gson().fromJson(teamDataJson, Quiz::class.java)

        QuestionDetailSeen(onQuestionComplete = onQuestionComplete, quiz =teamData )
    }
}

object QuestionDetailRoute {
    const val route = "question_detail"
}