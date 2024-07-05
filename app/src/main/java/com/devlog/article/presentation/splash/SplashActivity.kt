package com.devlog.article.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.devlog.article.R
import com.devlog.article.presentation.main.MainActivity
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.bookmark.BookmarkSharedPreferencesHelper
import com.devlog.article.presentation.my_keywords_select.AIDevelopment
import com.devlog.article.presentation.my_keywords_select.ITEquipment
import com.devlog.article.presentation.my_keywords_select.MyKeywordSelectActivity
import com.devlog.article.presentation.sign_in.SignInActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.article.presentation.my_keywords_select.Common
import com.devlog.article.presentation.my_keywords_select.DevelopmentCommon
import com.devlog.article.presentation.my_keywords_select.ITNews
import com.devlog.article.presentation.my_keywords_select.PM
import com.devlog.article.presentation.my_keywords_select.UIUXDesign
import com.devlog.article.presentation.my_keywords_select.WebDevelopment
import com.devlog.article.presentation.my_keywords_select.androidDevelopment
import com.devlog.article.presentation.my_keywords_select.iOSDevelopment
import com.devlog.article.presentation.my_keywords_select.serverDevelopment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity()  {
    lateinit var userPreference : UserPreference
    var viewModel = SplashViewMode()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()

        userPreference= UserPreference.getInstance(this)
        intent =Intent(this, MainActivity::class.java)
        if (signInCheck()){
            if (keywordCheck()){
                viewModel.getBookMaker()
                viewModel.fetchData()
                viewModel.getArticle(userPreference.getUserPagePassed())
            }

        }

    }

    fun observeData() = viewModel.profileSplashStateLiveData.observe(this) {
        when (it) {
            is SplashState.Uninitialized -> initView()
            is SplashState.Loading -> handlePostApi()
            is SplashState.GetBookMaker ->  handleBookMakerState(it)
            is SplashState.GetArticle ->{
                Log.e("polaris",count.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    Log.e("polaris1",count.toString())
                    handleArticleState(it)
                }


            }
            is SplashState.GetArticleFail ->{ count++}
        }
    }

    fun keywordCheck():Boolean{
        if (userPreference.userKeywordCheck){
            return  true
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,MyKeywordSelectActivity::class.java))
                finish()


            }, 1500)

        }
        return  false
    }
    fun signInCheck():Boolean {
        if (userPreference.userSignInCheck){
            return  true
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,SignInActivity::class.java))

            }, 1500)
        }
        return false
    }

    fun initView(){
        setContent{

            ArticleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Logo()
                }
            }
        }
    }

    fun handlePostApi(){
        viewModel.getBookMaker()
        viewModel.getArticle(userPreference.getUserPagePassed())
        for (i in 0..10){
            viewModel.getArticleKeyword(i, arrayListOf())
        }

    }

    fun handleBookMakerState(state: SplashState.GetBookMaker){
        BookmarkSharedPreferencesHelper(this).saveToSharedPreferences(state.bookMakerList)

    }

    var count =0
    var maxCount =11
    fun handleArticleState(state: SplashState.GetArticle) {
        count++
        Log.e("테스트 입니다",count.toString())
        when(state.category){
            Common ->{
                intent.putExtra("article_common",state.articleResponse)

            }
            DevelopmentCommon->{
                intent.putExtra("article_developmentCommon",state.articleResponse)
            }
            ITEquipment->{
                intent.putExtra("article_it_equipment",state.articleResponse)
            }
            androidDevelopment->{
                intent.putExtra("article_android_development",state.articleResponse)
            }
            serverDevelopment->{
                intent.putExtra("article_server_development",state.articleResponse)
            }
            WebDevelopment->{
                intent.putExtra("article_web_development",state.articleResponse)
            }
            AIDevelopment->{
                intent.putExtra("article_ai_development",state.articleResponse)
            }
            UIUXDesign->{
                intent.putExtra("article_ui_ux_design",state.articleResponse)
            }
            PM->{
                intent.putExtra("article_pm",state.articleResponse)
            }
            iOSDevelopment->{
                intent.putExtra("article_ios",state.articleResponse)
            }
            ITNews->{
                intent.putExtra("article_it_news",state.articleResponse)
            }
        }

        if (count == maxCount){
            intent.putExtra("article",viewModel.article)
            startActivity(intent)
            finish()
        }
    }


}
@Composable
fun Logo(){
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Image(painter = painterResource(id = R.drawable.article_logo), contentDescription = null,)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ArticleTheme {
        Logo()
    }
}