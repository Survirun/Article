package com.devlog.feature_splash

import android.app.Activity
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devlog.article.presentation.splash.intent.SplashIntent
import com.devlog.article.presentation.ui.theme.SplashTheme

import com.devlog.feature_splash.state.SplashApiState
import com.devlog.feature_splash.state.SplashUiState
import com.devlog.preference.PrefManager

//TODO 디자인 적용하기
@Composable
fun SplashScreen(viewModel: SplashViewModel2 = hiltViewModel(), resultReceiver : ResultReceiver, loginCheck:()->Unit, keywordCheck:()->Unit){

    val apiState by viewModel.apiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isApiFinished by viewModel.isUiFinished.collectAsState()


    LaunchedEffect(isApiFinished) {

        if (isApiFinished) {
            viewModel.onAllApiSuccess()
        }
    }
    when (apiState) {

        is SplashApiState.Loading ->{

        }
        is SplashApiState.GetArticleSuccess->{

            viewModel.article += (apiState as SplashApiState.GetArticleSuccess).articleResponseMap

        }
        is SplashApiState.GetArticleKeywordsSuccess ->{

            viewModel.article += (apiState as SplashApiState.GetArticleKeywordsSuccess).articleResponseMap

        }
        is SplashApiState.Failure ->{

        }
        else->{

        }

    }
    LaunchedEffect(uiState){
        when (uiState){

            is SplashUiState.Loding->{
                if (!PrefManager.userSignInCheck){
                    Log.e("polaris","로그인 안되있음")
                    loginCheck()

                }else if (!PrefManager.userKeywordCheck){
                    Log.e("polaris","키워드 체크 안되있음")
                    keywordCheck()
                }else{

                    viewModel.processIntent(SplashIntent.GetArticle)
                    viewModel.processIntent(SplashIntent.GetArticleKeywordList)
                    viewModel.processIntent(SplashIntent.GetBookMaker)
                }



            }
            is SplashUiState.Success->{

                Log.d("polaris","Success")


                val bundle = Bundle().apply {
                    putSerializable("data", HashMap(viewModel.article)) // Map을 Bundle에 저장할 수 있도록 변환
                }
                resultReceiver.send(Activity.RESULT_OK, bundle)


            }

        }
    }


    SplashTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {



            SplashScreenView2(viewModel)
        }
    }

}

@Composable
fun SplashScreenView2(viewModel: SplashViewModel2) {
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


            Log.d("polaris_snapshotFlow1", transitionProgress.toString())
            snapshotFlow { transitionProgress }
                .collect { progress ->
                    Log.d("polaris_snapshotFlow2", progress.toString())

                    // transitionProgress가 1f에 도달했을 때 화면 전환
                    if (progress == 1f) {
                        backgroundColor = (Color(0xFFFFFFFF))
                        showTransition = true
                        viewModel.test()

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
          //  (context as SplashActivity).window.statusBarColor = Color.White.toArgb()
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



