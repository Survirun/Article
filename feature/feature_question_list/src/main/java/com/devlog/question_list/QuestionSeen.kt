package com.devlog.question_list

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devlog.preference.PrefManager


@Composable
fun QuestionSeen(
    onQuestionClick: () -> Unit = {},
    viewModel: QuestionViewModel = hiltViewModel()
) {
    val questionTitleLis by viewModel.questionTitleLis.collectAsState(emptyList()) // StateFlow 관찰

    viewModel.getQuestionTitleList()
    QuestionSeenView(questionTitleLis) {
        Log.d("polaris", "클릭")
        onQuestionClick()
    }
}

@Composable
fun QuestionSeenView(questionTitleLis: List<String>, onClick: () -> Unit) {
    Column {
        LazyColumn() {
            itemsIndexed(questionTitleLis, key = { _, item -> item }) { index, item ->
                Surface(modifier = Modifier.padding(10.dp),
                    onClick = {
                        if (PrefManager.day >= index) {
                            onClick()
                            PrefManager.day = index
                        }
                    }) {
                    QuestionItem(index, item)
                }


            }

        }
    }
}

@Composable
fun QuestionItem(index: Int, titls: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(vertical = 10.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Log.d("polaris_day",(PrefManager.day).toString())
        Log.d("polaris_day",(index).toString())
        Text(text = titls, color = if (PrefManager.day>= index) Color(0xFF000000) else Color(0xFFB1B1B1))
    }


}

@Composable
@Preview(showBackground = true)
fun preView() {
    val dummyList = listOf("테스트1", "테스트2", "테스트3", "테스트4")
    QuestionSeenView(dummyList) {}
}