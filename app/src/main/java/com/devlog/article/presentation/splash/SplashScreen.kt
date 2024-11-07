package com.devlog.article.presentation.splash

import android.app.Activity
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devlog.article.R
import com.devlog.article.presentation.ui.theme.SplashTheme
import com.devlog.article.utility.UtilManager.keywordCheck
import com.devlog.article.utility.UtilManager.signInCheck
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun SplashScreen2(viewModel: SplashViewModel2= hiltViewModel(),resultReceiver : ResultReceiver){

    val apiState by viewModel.apiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()


    when (apiState) {
        is SplashApiState.GetArticleSuccess->{

            viewModel.article += (apiState as SplashApiState.GetArticleSuccess).articleResponseMap

        }
        is SplashApiState.GetArticleKeywordsSuccess ->{

            viewModel.article+= (apiState as SplashApiState.GetArticleKeywordsSuccess).articleResponseMap

        }
        is SplashApiState.Failure ->{

        }
        else->{

        }

    }
    when (uiState){
        is SplashUiState.Loding->{

        }
        is SplashUiState.Success->{

            Log.d("polaris","Success")


            val bundle = Bundle().apply {
                putSerializable("data", HashMap(viewModel.article)) // Map을 Bundle에 저장할 수 있도록 변환
            }
            resultReceiver.send(Activity.RESULT_OK, bundle)


        }

    }

    SplashTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {



            SplashScreenView2(viewModel,resultReceiver)
        }
    }

}

@Composable
fun SplashScreenView2(viewModel: SplashViewModel2,resultReceiver:ResultReceiver) {
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
            viewModel.onAllApiSuccess()


        } else {
            LottieAnimation(
                composition = spinningComposition,
                progress = { spinningProgress },
                modifier = Modifier.fillMaxSize(1f)
            )


        }
    }

}



