package com.devlog.article.presentation.question_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.devlog.article.data.entity.question.Question
import kotlinx.coroutines.delay
import com.devlog.article.data.preference.PrefManager


@Composable
fun QuestionDetailSeen(viewModel: QuestionDetailViewModel = hiltViewModel(),onQuestionComplete: () -> Unit) {


    viewModel.getQuestionUseCase(PrefManager.day+1)
    viewModel.onQuestionComplete = { onQuestionComplete() }
    Box(modifier = Modifier.fillMaxSize()) {

            AnswerCheckView(
                isAnswerCorrect = viewModel.isAnswerTrueCorrect.collectAsState().value,
                isAnswerIncorrect = viewModel.isAnswerFalseCorrect.collectAsState().value
            ) {
                viewModel.clearAnswerStatus()
            }
            QuestionDetailView(viewModel.question.collectAsState().value){
                viewModel.questionCorrectAnswer(it)
            }


    }


}

@Composable()
fun QuestionTitle(title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
    }


}


@Composable
fun AnswerOptions(choiceTitle: String,  onClick:(String) -> Unit ) {

    Surface(onClick = {onClick(choiceTitle)}) {
        Text(text = choiceTitle, modifier = Modifier.padding(30.dp))
    }

}

@Composable
fun QuestionDetailView(question: Question,click: (String) -> Unit ={}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        QuestionSection(question.question, modifier = Modifier.weight(0.6f))
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.4f),
        ) {
            Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                AnswerOptions(  question.options[0],click)
                AnswerOptions( question.options[1],click)
            }
            Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                AnswerOptions( question.options[2],click)
                AnswerOptions(  question.options[3],click)
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun PreView() {
    val dummyQuestion = Question(
        1,
        category = "HTML",
        question = "<h1> 태그의 역할은 무엇인가요?",
        options = listOf("페이지 제목", "이미지 삽입", "하이퍼링크", "본문 텍스트"),
        answer = "<img>",
        explanation = "<img> 태그는 HTML에서 이미지를 삽입하는 데 사용되며 src 속성을 통해 경로를 지정합니다."
    )
    QuestionDetailView(dummyQuestion)

}

@Composable
fun QuestionSection(questionText: String, modifier: Modifier = Modifier) {
    QuestionTitle(
        title = questionText,
        modifier = modifier.fillMaxWidth()


    )
}


@Composable
@Preview(showBackground = true)
fun AnswerCheckView(
    isAnswerCorrect: Boolean = true,
    isAnswerIncorrect: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    if (isAnswerCorrect || isAnswerIncorrect) {
        val message = if (isAnswerCorrect) "정답입니다!" else "오답입니다!"
        val backgroundColor = if (isAnswerCorrect) Color.Green else Color.Red

        LaunchedEffect(isAnswerCorrect, isAnswerIncorrect) {
            // 1초 뒤에 onDismiss 호출
            delay(1000)
            onDismiss()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .background(Color(0x3B000000)), // 반투명 배경
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }
    }
}