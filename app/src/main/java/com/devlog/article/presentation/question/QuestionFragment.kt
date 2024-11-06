package com.devlog.article.presentation.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.devlog.article.R
import com.devlog.article.presentation.question_compensation.navigateQuestionCompensation
import com.devlog.article.presentation.question_compensation.questionCompensationNavGraph
import com.devlog.article.presentation.question_detail.navigateQuestionDetail
import com.devlog.article.presentation.question_detail.questionDetailNavGraph
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class QuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = QuestionRoute.route
                ) {
                    questionNavGraph(
                        onQuestionClick = { navController.navigateQuestionDetail() }
                    )
                    questionDetailNavGraph(onQuestionComplete = { navController.navigateQuestionCompensation()})

                    questionCompensationNavGraph(onComplete = { navController.navigateQuestion()})


                }

            }

        }



    }


}