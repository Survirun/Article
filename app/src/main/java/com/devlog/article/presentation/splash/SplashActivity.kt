package com.devlog.article.presentation.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.window.SplashScreenView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieListener
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationState
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devlog.article.R
import com.devlog.article.presentation.main.MainActivity
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.bookmark.BookmarkSharedPreferencesHelper
import com.devlog.article.presentation.main.MainViewModel
import com.devlog.article.presentation.my_keywords_select.AIDevelopment
import com.devlog.article.presentation.my_keywords_select.ITEquipment
import com.devlog.article.presentation.my_keywords_select.MyKeywordSelectActivity
import com.devlog.article.presentation.sign_in.SignInActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.article.presentation.my_keywords_select.Common
import com.devlog.article.presentation.my_keywords_select.DEVCOMMON
import com.devlog.article.presentation.my_keywords_select.DevelopmentCommon
import com.devlog.article.presentation.my_keywords_select.ITNews
import com.devlog.article.presentation.my_keywords_select.PM
import com.devlog.article.presentation.my_keywords_select.UIUXDesign
import com.devlog.article.presentation.my_keywords_select.WebDevelopment
import com.devlog.article.presentation.my_keywords_select.androidDevelopment
import com.devlog.article.presentation.my_keywords_select.iOSDevelopment
import com.devlog.article.presentation.my_keywords_select.serverDevelopment
import com.devlog.article.presentation.ui.theme.SplashTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    lateinit var userPreference: UserPreference
    val viewModel : SplashViewModel by viewModels()
    lateinit var intentCustom: Intent
    var getApiKeywordList = arrayListOf(0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 12)

    var count = 0
    var maxCount = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContent {

            SplashTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    window.statusBarColor = Color.Black.toArgb()
                    SplashScreen(viewModel)
                }
            }
        }
        observeData()

        userPreference = UserPreference.getInstance(this)
        intentCustom = Intent(this, MainActivity::class.java)
        if (signInCheck()) {
            if (keywordCheck()) {
                viewModel.getArticleSeveralKeyword(arrayListOf( 1))
               // viewModel.getBookMaker()
                //viewModel.fetchData()
                //viewModel.getArticle()
            }

        }

    }

    fun observeData() = viewModel.profileSplashStateLiveData.observe(this) {
        when (it) {
            is SplashState.Initialize ->{
                if (signInCheck()) {
                    if (keywordCheck()) {
                      viewModel.fetchData()
                    }

                }
            }
            is SplashState.Loading -> handlePostApi()
            is SplashState.GetBookMaker -> handleBookMakerState(it)
            is SplashState.GetArticle -> {
                CoroutineScope(Dispatchers.IO).launch {
                    handleArticleState(it)
                }


            }

            is SplashState.GetArticleFail -> {
                count++
            }
        }
    }

    fun keywordCheck(): Boolean {
        if (userPreference.userKeywordCheck) {
            return true
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MyKeywordSelectActivity::class.java))
                finish()


            }, 1500)

        }
        return false
    }

    fun signInCheck(): Boolean {
        if (userPreference.userSignInCheck) {
            return true
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, SignInActivity::class.java))

            }, 1500)
        }
        return false
    }




    fun handlePostApi() {
        //viewModel.getBookMaker()
        //viewModel.getArticle()
        viewModel.getArticleSeveralKeyword(arrayListOf(0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 12))
        getApiKeywordList.forEach {
        //    viewModel.getArticleKeyword(it, arrayListOf())
        }


    }

    fun handleBookMakerState(state: SplashState.GetBookMaker) {
        BookmarkSharedPreferencesHelper(this).saveToSharedPreferences(state.bookMakerList)

    }

    fun handleArticleState(state: SplashState.GetArticle) {
        //TODO 여기서 부터 작업 필요
        count++

        if (count == maxCount) {
            val bundle = Bundle()
            bundle.putSerializable("map", state.articleResponseMap as Serializable)
            intentCustom.putExtra("article", viewModel.article)
            intentCustom.putExtra("article_map",bundle)
            viewModel.isApiFinished=true
        }
    }


}

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    var showTransition by remember { mutableStateOf(false) }
    val spinningComposition by rememberLottieComposition(LottieCompositionSpec.Asset("splash_spinning.json"))
    val transitionComposition by rememberLottieComposition(LottieCompositionSpec.Asset("splash_transition.json"))
    val spinningProgress by animateLottieCompositionAsState(
        spinningComposition,
        iterations = LottieConstants.IterateForever
    )
    val transitionProgress by animateLottieCompositionAsState(transitionComposition, iterations = 1)
    val context = LocalContext.current
    var backgroundColor by remember { mutableStateOf(Color(0xFF17161D)) }


    LaunchedEffect(viewModel.isApiFinished) {
        if (viewModel.isApiFinished) {
            backgroundColor = (Color(0xFFFFFFFF))
            showTransition = true
            viewModel.viewModelScope.launch(Dispatchers.Main) {

                if (transitionProgress == 1f) {
                    composeStartActivity(context)
                }

            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(backgroundColor)
    ) {
        if (showTransition) {
            (context as SplashActivity).window.statusBarColor = Color.White.toArgb()
            LottieAnimation(
                composition = transitionComposition,
                progress = { transitionProgress },
                modifier = Modifier.fillMaxSize(1f)
            )
        } else {
            LottieAnimation(
                composition = spinningComposition,
                progress = { spinningProgress },
                modifier = Modifier.fillMaxSize(1f)
            )
        }
    }

}

fun composeStartActivity(context: android.content.Context) {

    val options = ActivityOptions.makeCustomAnimation(context, 0, 0)
    (context as? SplashActivity)!!.startActivity(
        (context as? SplashActivity)!!.intentCustom,
        options.toBundle()
    )
    (context as? SplashActivity)!!.finish()

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

   // SplashScreen()
}

@Composable
fun Logo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_app_logo_foreground),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Logo()
    }

}