package com.devlog.question_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devlog.model.data.entity.response.quiz.Quiz
import com.devlog.preference.PrefManager
import com.devlog.question_list.intent.QuestionIntent
import com.devlog.question_list.state.QuestionApiState


@Composable
fun QuestionSeen(
    onQuestionClick: (quiz:Quiz) -> Unit = {},
    viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionApiState by viewModel.questionTitleLis.collectAsState()
    val quizList= viewModel.quizList.collectAsState()
    viewModel.processIntent(QuestionIntent.GetQuestion)
    when(questionApiState){
        is  QuestionApiState.Initialize->{

        }
        is QuestionApiState.QuestionSuccess->{
             (questionApiState as  QuestionApiState.QuestionSuccess).quizResponse


        }

    }
    QuestionSeenView(quizList.value, onClick =onQuestionClick)

}

@Composable
fun QuestionSeenView(questionTitleLis: List<Quiz>, onClick: (Quiz) -> Unit) {

    Column(modifier = Modifier.fillMaxSize(1f)) {
        LazyColumn() {
            itemsIndexed(questionTitleLis, key = { _, item -> item }) { index, quiz ->
                Surface(modifier = Modifier.padding(10.dp),
                    onClick = {
                        if (PrefManager.day >= index) {

                            onClick(quiz)
                            PrefManager.day = index
                        }
                    }) {
                    QuestionItem(index, quiz.title,PrefManager.day)
                }


            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun QuestionItem(index: Int =0, titls: String ="문제 입니다",day:Int=0) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(vertical = 10.dp)

        ) {
//        Log.d("polaris_day",(PrefManager.day).toString())
//        Log.d("polaris_day",(index).toString())
        Text(text = titls, color = if (day>= index) Color(0xFF000000) else Color(0xFFB1B1B1), fontSize = 16.sp)
        Text(text = titls, color = if (day>= index) Color(0xFF000000) else Color(0xFFB1B1B1), fontSize = 14.sp)
    }


}
@Composable
@Preview(showBackground = true)
fun PreViewQuestionSeenView() {
    val dumyList = listOf(
        Quiz("HTML 기본 태그", 0, questions = listOf()),
        Quiz("CSS 기본 속성", 0, questions = listOf()),
        Quiz("JavaScript 개요", 0, questions = listOf())
    )

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(dumyList, key = { index, _ -> index }) { index, quiz ->
                Surface(modifier = Modifier.padding(10.dp), onClick = {}) {
                    QuestionItem(index = index, titls = quiz.title, day = 1) // 가짜 값 사용
                }
            }
        }
    }
}

