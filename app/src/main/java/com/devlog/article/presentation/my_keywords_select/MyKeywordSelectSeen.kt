package com.devlog.article.presentation.my_keywords_select

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.devlog.article.presentation.ui.theme.BaseColumn
import com.devlog.article.presentation.ui.theme.HeaderView
import dagger.hilt.android.qualifiers.ApplicationContext



@Composable
fun MyKeywordSelectSeen( viewModel:MyKeywordSelectViewModel2= hiltViewModel(),onComplete: () -> Unit) {
    val context = LocalContext.current
    val apiState by viewModel.apiState.collectAsState()
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
    when(apiState){
       is  MyKeywordSelectApiState.keywordLoading ->{

       }
        is MyKeywordSelectApiState.postKeywordSuccess ->{
            PrefManager.userKeywordCheck=true
            onComplete()

        }
        is MyKeywordSelectApiState.postKeywordFail->{
            Toast.makeText(context,"잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
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
        KeywordSelectButton(viewModel)
    }
}

@Composable
fun KeywordSelectButton(viewModel: MyKeywordSelectViewModel2){
    val count = viewModel.count.collectAsState()
    val backgroundColor : Any
    Log.d("polaris",count.toString())
    backgroundColor = if(count.value>=3) 0xFF000000 else 0xFFA0A0AB
    val infoText : String = if(count.value>=3) "총 ${count.value}개 선택" else "3가지 이상의 주제를 선택해주세요."
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

                        viewModel.processIntent(MyKeywordSelectIntent.postMyKeywordSelectPost(list.toTypedArray()))
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