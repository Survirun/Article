package com.devlog.feature_question_detail

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devlog.article.presentation.ui.theme.CorrectAnswer
import com.devlog.article.presentation.ui.theme.Gray10
import com.devlog.article.presentation.ui.theme.Gray100
import com.devlog.article.presentation.ui.theme.Gray20
import com.devlog.article.presentation.ui.theme.Gray30
import com.devlog.article.presentation.ui.theme.Gray40
import com.devlog.article.presentation.ui.theme.WrongAnswer
import com.devlog.designsystem.R.drawable.ic_back_arrow
import com.devlog.model.data.entity.response.quiz.Question
import com.devlog.model.data.entity.response.quiz.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuestionDetailSeen(
    viewModel: QuestionDetailViewModel = hiltViewModel(),
    quiz: Quiz,
    onQuestionComplete: () -> Unit
) {

    viewModel.onQuestionComplete = { onQuestionComplete() }
    viewModel.updateQuiz(quiz)
    val animatedProgress by animateFloatAsState(
        targetValue = viewModel.progress.collectAsState().value,
        animationSpec = tween(
            durationMillis = 1000 // 애니메이션 지속 시간 (1초)
        ), label = ""
    )

    QuestionDetailView(
        quiz.title,
        viewModel.question.collectAsState().value,
        animatedProgress
    ) {

        viewModel.questionCorrectAnswer(it)
        CoroutineScope(Dispatchers.Default).launch {
            delay(1500L)
            viewModel.clearAnswerStatus()
        }

    }


}

@Composable()
fun QuestionTitle(title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, fontSize = 26.sp, textAlign = TextAlign.Center)
    }


}


@Composable
fun AnswerOptions(
    index: Int,
    choiceTitle: String,
    onClick: (String) -> Unit,
    viewModel: QuestionDetailViewModel = hiltViewModel()
) {
    val isAnswerFalseCorrect = viewModel.isAnswerFalseCorrect.collectAsState().value
    val isAnswerTrueCorrect = viewModel.isAnswerTrueCorrect.collectAsState().value
    val userCheckResponse = viewModel.userCheckResponse.collectAsState().value


    val backGroundColor = remember(isAnswerFalseCorrect, isAnswerTrueCorrect) {
        when {
            isAnswerFalseCorrect && userCheckResponse == choiceTitle -> WrongAnswer
            isAnswerTrueCorrect && userCheckResponse == choiceTitle -> CorrectAnswer
            else -> Gray20
        }
    }

    val textColor = remember(isAnswerFalseCorrect, isAnswerTrueCorrect) {
        when {
            isAnswerFalseCorrect && userCheckResponse == choiceTitle -> Gray10
            isAnswerTrueCorrect && userCheckResponse == choiceTitle -> Gray10
            !isAnswerTrueCorrect && !isAnswerFalseCorrect -> Gray100
            isAnswerTrueCorrect || isAnswerFalseCorrect -> Gray40
            else -> Gray20
        }
    }

    val numberTextColor = remember(isAnswerFalseCorrect, isAnswerTrueCorrect) {
        when {
            isAnswerFalseCorrect && userCheckResponse == choiceTitle -> Gray100
            isAnswerTrueCorrect && userCheckResponse == choiceTitle -> Gray100
            !isAnswerTrueCorrect && !isAnswerFalseCorrect -> Gray100
            isAnswerTrueCorrect || isAnswerFalseCorrect -> Gray40
            else -> Gray20
        }
    }


    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { onClick(choiceTitle) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .background(backGroundColor)
                .padding(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(92.dp),
                modifier = Modifier.size(24.dp),
                color = Gray20
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = (index + 1).toString(), color = numberTextColor)
                }

            }

            Text(
                text = choiceTitle, modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 8.dp), color = textColor
            )
        }
    }

}

@Composable
fun QuestionDetailView(
    title: String,
    question: Question,
    progress: Float = 0.0f,
    click: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Gray20),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                Image(
                    painter = painterResource(ic_back_arrow),
                    "backButtonImage ",
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .clickable { backDispatcher?.onBackPressed() })

                Text(text = title)
            }
            Surface(
                shape = RoundedCornerShape(60.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                LinearProgressIndicator(
                    progress = progress,
                    color= Gray100,
                    backgroundColor = Gray10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )

            }

        }

        QuestionTitle(question.question)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            RoundedCornerShape(12.dp),
            border = BorderStroke(width = 1.dp, color = Gray30)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 4.dp)
            ) {
                itemsIndexed(question.options, key = { index, _ -> index }) { index, options ->
                    AnswerOptions(index, options, click)
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun PreView() {
    val dummyQuestion = Question(
        "1",
        category = "HTML",
        question = "<h1> 태그의 역할은 무엇인가요?",
        options = listOf("페이지 제목", "이미지 삽입", "하이퍼링크", "본문 텍스트"),
        answer = "<img>",
        explanation = "<img> 태그는 HTML에서 이미지를 삽입하는 데 사용되며 src 속성을 통해 경로를 지정합니다."
    )
    QuestionDetailView("HTML", dummyQuestion)

}


