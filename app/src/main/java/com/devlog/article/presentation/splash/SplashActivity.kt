package com.devlog.article.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devlog.article.R
import com.devlog.article.presentation.main.MainActivity
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.my_keywords_select.MyKeywordSelectActivity
import com.devlog.article.presentation.sign_in.SignInActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity()  {
    lateinit var userPreference : UserPreference
    lateinit var viewModel:SplashViewMode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel= SplashViewMode()
            viewModel.succeed={

                val intent =Intent(this, MainActivity::class.java)
                intent.putExtra("article",viewModel.article)
                startActivity(intent)
                finish()

            }
            viewModel.failed={

            }
            userPreference= UserPreference.getInstance(this)
            if ( userPreference.userSignInCheck){
                if (userPreference.userKeywordCheck){

                   viewModel.getArticle()
                }else{
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this,MyKeywordSelectActivity::class.java))
                        finish()


                    }, 1500)

                }

            }else{
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this,SignInActivity::class.java))

                }, 1500)

            }

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