package com.devlog.article.presentation.question_compensation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun QuestionCompensationSeen(onComplete:() -> Unit){
    Column(modifier = Modifier.fillMaxSize(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Button(onClick = { onComplete() }, ) {
            Text(text = "학습을 완료 했어요")
        }
    }
}