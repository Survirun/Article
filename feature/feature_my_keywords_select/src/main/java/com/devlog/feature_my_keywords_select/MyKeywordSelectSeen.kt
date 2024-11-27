package com.devlog.feature_my_keywords_select

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devlog.date.entity.article.AIDevelopment
import com.devlog.date.entity.article.ITNews
import com.devlog.date.entity.article.PM
import com.devlog.date.entity.article.UIUXDesign
import com.devlog.date.entity.article.WebDevelopment
import com.devlog.date.entity.article.androidDevelopment
import com.devlog.date.entity.article.iOSDevelopment
import com.devlog.date.entity.article.serverDevelopment
import com.devlog.designsystem.theme.BaseColumn
import com.devlog.designsystem.theme.HeaderView
import com.devlog.feature_my_keywords_select.intent.MyKeywordSelectIntent
import com.devlog.feature_my_keywords_select.state.MyKeywordSelectApiState
import com.devlog.preference.PrefManager

data class KeywordSelectData(var name: String, var selectData: MutableState<Boolean>, var code:Int)
@Composable
fun MyKeywordSelectSeen(
    viewModel: MyKeywordSelectViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val apiState by viewModel.apiState.collectAsState()

   val keywordList = arrayListOf(
        KeywordSelectData("IT 소식 📢", remember { mutableStateOf(false) }, ITNews),
        KeywordSelectData("Android 개발 📱", remember { mutableStateOf(false) }, androidDevelopment),
        KeywordSelectData("iOS 개발 🍎", remember { mutableStateOf(false) }, iOSDevelopment),
        KeywordSelectData("Web 개발 🌐", remember { mutableStateOf(false) }, WebDevelopment),
        KeywordSelectData("서버 개발 🎒", remember { mutableStateOf(false) }, serverDevelopment),
        KeywordSelectData("AI 개발 🤖", remember { mutableStateOf(false) }, AIDevelopment),
        KeywordSelectData("UIUX 디자인 🎨", remember { mutableStateOf(false) }, UIUXDesign),
        KeywordSelectData("기획 📃", remember { mutableStateOf(false) }, PM),
    )

    when (apiState) {
        is MyKeywordSelectApiState.keywordLoading -> {

        }

        is MyKeywordSelectApiState.postKeywordSuccess -> {
            PrefManager.userKeywordCheck = true
            onComplete()

        }

        is MyKeywordSelectApiState.postKeywordFail -> {
            Toast.makeText(context, "잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        BaseColumn {
            HeaderView()
            Title()
            KeywordSelectList(keywordList, viewModel2 = viewModel)

        }
        KeywordSelectButton(keywordList,viewModel)
    }
}

@Composable
fun Title() {
    Text(
        modifier = Modifier.padding(top = 48.dp, bottom = 16.dp),
        fontSize = 26.sp,
        fontWeight = FontWeight(600),
        lineHeight = 40.sp,
        text = "최근 관심있는\n주제를 선택해주세요"
    )

}


@Composable
fun Keywords(keywordSelectData: KeywordSelectData, viewModel2: MyKeywordSelectViewModel) {
    val borderColor: Color
    val background: Color
    val fontColor: Color
    if (keywordSelectData.selectData.value) {
        background = Color(0xFF18171D)
        borderColor = Color(0xFF18171D)
        fontColor = Color(0xFFFFFFFF)

    } else {
        background = Color(0xFFFAFAFC)
        borderColor = Color(0xFFEEEEF0)
        fontColor = Color(0xFF3F3F46)
    }

    Surface(
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(1.dp, borderColor),
        color = background,

        ) {
        Column(
            modifier = Modifier.clickable {
                keywordSelectData.selectData.value = !keywordSelectData.selectData.value
                if (keywordSelectData.selectData.value) {

                    viewModel2.countPlus()

                } else {
                    viewModel2.countMinus()
                }
            }, verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                text = keywordSelectData.name,
                fontSize = 16.sp,
                color = fontColor
            )
        }

    }
}

@Composable
fun KeywordSelectList(keywordList: List<KeywordSelectData>, viewModel2: MyKeywordSelectViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        items(keywordList) {
            Keywords(it, viewModel2)
        }
    }
}


@Composable
fun KeywordSelectButton(keywordList:ArrayList<KeywordSelectData>,viewModel: MyKeywordSelectViewModel) {
    val count = viewModel.count.collectAsState()
    val backgroundColor: Any
    Log.d("polaris", count.toString())
    backgroundColor = if (count.value >= 3) 0xFF000000 else 0xFFA0A0AB
    val infoText: String =
        if (count.value >= 3) "총 ${count.value}개 선택" else "3가지 이상의 주제를 선택해주세요."
    Column(
        modifier = Modifier
            .fillMaxSize(1f),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(60.dp)
                .background(Color(backgroundColor))
                .clickable(enabled = (count.value >= 3)) {
                    if (count.value >= 3) {
                        val list = arrayListOf<Int>()
                        keywordList.forEach {
                            if (it.selectData.value) {
                                list.add(it.code)
                            }
                        }

                        viewModel.processIntent(
                            MyKeywordSelectIntent.postMyKeywordSelectPost(
                                list.toTypedArray()
                            )
                        )
                    }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = infoText,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
