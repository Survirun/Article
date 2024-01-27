package com.devlog.article.presentation.my_keywords_select

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.article.presentation.ui.theme.BaseColumn


class MyKeywordSelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArticleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}
data class keywordSelectData(var name:String,var selectData: Boolean)
@Composable
@Preview(showBackground = true)
fun Preview() {

    ArticleTheme {
        val list = listOf<keywordSelectData>(
            keywordSelectData("IT기기",false),
            keywordSelectData("IT 소식",false),
            keywordSelectData(  "Android 개발",false),
            keywordSelectData(  "iOS 개발",false),
            keywordSelectData(  "Web 개발",false),
            keywordSelectData(  "서버 개발",false),
            keywordSelectData(  "AI 개발",false),
            keywordSelectData(  "UIUX 디자인",false),
            keywordSelectData(  "기획",false),
        )


        BaseColumn {
            //HeaderView()
            Title()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                items(list) {
                    Keywords(it)
                }
            }

        }
    }


}

@Composable
fun Title() {
    Text(
        modifier = Modifier.padding(top = 48.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        fontSize = 26.sp,
        fontWeight = FontWeight(600) ,
        lineHeight =40.sp,
        text = "최근 관심있는\n주제를 선택해주세요"
    )

}

@Composable
fun Keywords(keywordSelectData:keywordSelectData) {
    Surface(shape = RoundedCornerShape(17.dp)) {
        Text(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            text = keywordSelectData.name,
            fontSize = 16.sp
        )
    }
}