package com.devlog.article.presentation.my_keywords_select

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devlog.article.data.entity.article.AIDevelopment
import com.devlog.article.data.entity.article.ITNews
import com.devlog.article.data.entity.article.KeywordSelectData
import com.devlog.article.data.entity.article.PM
import com.devlog.article.data.entity.article.UIUXDesign
import com.devlog.article.data.entity.article.WebDevelopment
import com.devlog.article.data.entity.article.androidDevelopment
import com.devlog.article.data.entity.article.iOSDevelopment
import com.devlog.article.data.entity.article.serverDevelopment
import com.devlog.article.data.preference.PrefManager
import com.devlog.article.presentation.splash.SplashActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.article.presentation.ui.theme.BaseColumn
import com.devlog.article.presentation.ui.theme.HeaderView
import dagger.hilt.android.AndroidEntryPoint

lateinit var count: MutableState<Int>
var keywordList = listOf<KeywordSelectData>()
@AndroidEntryPoint
class MyKeywordSelectActivity : ComponentActivity() {
    var viewModel=MyKeywordSelectViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.failed ={
            Toast.makeText(this,"잠시후 다시 시도해주세요",Toast.LENGTH_SHORT).show()
        }
        viewModel.succeedPathMyKeywords ={
            PrefManager.userKeywordCheck=true
            val intent =Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()

        }

        setContent {
            count = remember {
                mutableStateOf(0)
            }
            keywordList = listOf(
                KeywordSelectData("IT 소식 📢", remember { mutableStateOf(false) }, ITNews),
                KeywordSelectData("Android 개발 📱", remember { mutableStateOf(false) },
                    androidDevelopment
                ),
                KeywordSelectData("iOS 개발 🍎", remember { mutableStateOf(false) }, iOSDevelopment),
                KeywordSelectData("Web 개발 🌐", remember { mutableStateOf(false) }, WebDevelopment),
                KeywordSelectData("서버 개발 🎒", remember { mutableStateOf(false) }, serverDevelopment),
                KeywordSelectData("AI 개발 🤖", remember { mutableStateOf(false) }, AIDevelopment),
                KeywordSelectData("UIUX 디자인 🎨", remember { mutableStateOf(false) }, UIUXDesign),
                KeywordSelectData("기획 📃", remember { mutableStateOf(false) }, PM),
            )

            ArticleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    BaseColumn {
                        HeaderView()
                        Title()
                       // KeywordSelectList(keywordList, viewModel2 = )

                    }
                       // KeywordSelectButton(viewModel = )
                }
            }
        }
    }
}


    @Composable
    @Preview(showBackground = true)
    fun Preview() {

        count = remember {
            mutableStateOf(0)
        }
        keywordList = listOf(
            KeywordSelectData("IT 소식 📢", remember { mutableStateOf(false) },10),
            KeywordSelectData("Android 개발 📱", remember { mutableStateOf(false) },
                androidDevelopment
            ),
            KeywordSelectData("iOS 개발 🍎", remember { mutableStateOf(false) },9),
            KeywordSelectData("Web 개발 🌐", remember { mutableStateOf(false) }, WebDevelopment),
            KeywordSelectData("서버 개발 🎒", remember { mutableStateOf(false) }, serverDevelopment),
            KeywordSelectData("AI 개발 🤖", remember { mutableStateOf(false) }, AIDevelopment),
            KeywordSelectData("UIUX 디자인 🎨", remember { mutableStateOf(false) }, UIUXDesign),
            KeywordSelectData("기획 📃", remember { mutableStateOf(false) }, PM),
        )

        ArticleTheme {

            BaseColumn {
                HeaderView()
                Title()
               // KeywordSelectList(keywordList)

            }
               // KeywordSelectButton()
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
    fun Keywords(keywordSelectData: KeywordSelectData,viewModel2: MyKeywordSelectViewModel2) {
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
    fun KeywordSelectList(keywordList:List<KeywordSelectData>,viewModel2: MyKeywordSelectViewModel2){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            items(keywordList) {
                Keywords(it,viewModel2)
            }
        }



}