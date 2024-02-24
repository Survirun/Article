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
import com.devlog.article.R
import com.devlog.article.presentation.main.MainActivity
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.article.presentation.ui.theme.BaseColumn
import com.devlog.article.presentation.ui.theme.HeaderView

lateinit var count: MutableState<Int>
var keywordList = listOf<KeywordSelectData>()

class MyKeywordSelectActivity : ComponentActivity() {
    var viewModel=MyKeywordSelectViewModel()
    lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreference=UserPreference.getInstance(this)
        viewModel.succeedGetArticle={
            userPreference.userKeywordCheck=true
            val intent =Intent(this, MainActivity::class.java)
            intent.putExtra("article",viewModel.article)
            startActivity(intent)
            finish()
        }
        viewModel.failed ={
            Toast.makeText(this,"잠시후 다시 시도해주세요",Toast.LENGTH_SHORT).show()
        }
        viewModel.succeedPathMyKeywords ={
            viewModel.getArticle(userPreference.getUserPagePassed())

        }

        setContent {
            count = remember {
                mutableStateOf(0)
            }
            keywordList = listOf(
                KeywordSelectData("IT기기 💻", remember { mutableStateOf(false) },ITEquipment),
                KeywordSelectData("IT 소식 📢", remember { mutableStateOf(false) },ITNews),
                KeywordSelectData("Android 개발 📱", remember { mutableStateOf(false) },androidDevelopment),
                KeywordSelectData("iOS 개발 🍎", remember { mutableStateOf(false) },iOSDevelopment),
                KeywordSelectData("Web 개발 🌐", remember { mutableStateOf(false) },WebDevelopment),
                KeywordSelectData("서버 개발 🎒", remember { mutableStateOf(false) },serverDevelopment),
                KeywordSelectData("AI 개발 🤖", remember { mutableStateOf(false) },AIDevelopment),
                KeywordSelectData("UIUX 디자인 🎨", remember { mutableStateOf(false) },UIUXDesign),
                KeywordSelectData("기획 📃", remember { mutableStateOf(false) },PM),
            )

            ArticleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    BaseColumn {
                        HeaderView()
                        Title()
                        KeywordSelectList(keywordList)

                    }
                        KeywordSelectButton(){
//                            var list:Array<String> = Array(10) {
//                                keywordList.forEach{
//                                    if (it.selectData.value){
//                                        it.name
//                                    }
//                                }.toString()
//                            }
                            if(count.value >= 3){
                                var list = arrayListOf<Int>()
                                keywordList.forEach {
                                    if (it.selectData.value){
                                        list.add(it.code)
                                    }
                                }


                                viewModel.pathMyKeywords(list.toTypedArray())
                            }
                        }
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
            KeywordSelectData("IT기기 💻", remember { mutableStateOf(false) },ITEquipment),
            KeywordSelectData("IT 소식 📢", remember { mutableStateOf(false) },10),
            KeywordSelectData("Android 개발 📱", remember { mutableStateOf(false) },androidDevelopment),
            KeywordSelectData("iOS 개발 🍎", remember { mutableStateOf(false) },9),
            KeywordSelectData("Web 개발 🌐", remember { mutableStateOf(false) },WebDevelopment),
            KeywordSelectData("서버 개발 🎒", remember { mutableStateOf(false) },serverDevelopment),
            KeywordSelectData("AI 개발 🤖", remember { mutableStateOf(false) },AIDevelopment),
            KeywordSelectData("UIUX 디자인 🎨", remember { mutableStateOf(false) },UIUXDesign),
            KeywordSelectData("기획 📃", remember { mutableStateOf(false) },PM),
        )

        ArticleTheme {

            BaseColumn {
                HeaderView()
                Title()
                KeywordSelectList(keywordList)

            }
                KeywordSelectButton(){

                }

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
    fun Keywords(keywordSelectData: KeywordSelectData) {
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
                        count.value++

                    } else {
                        count.value--
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
    fun KeywordSelectList(keywordList:List<KeywordSelectData>){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            items(keywordList) {
                Keywords(it)
            }
        }
    }
    @Composable
    fun KeywordSelectButton(click :()->Unit){
        val backgroundColor : Any
        backgroundColor = if(count.value>=3) 0xFF000000
        else 0xFFA0A0AB
        val infoText : String = if(count.value>=3) "총 ${count.value}개 선택"
        else "3가지 이상의 주제를 선택해주세요."
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
                    .clickable(enabled = (count.value>=3)) {click() },
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