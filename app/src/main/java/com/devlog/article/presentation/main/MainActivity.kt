package com.devlog.article.presentation.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.response.Data

import com.devlog.article.presentation.article.state.ArticleTabState
import com.devlog.article.presentation.article.navigation.articleNavGraph
import com.devlog.article.presentation.article.navigation.articleRoute
import com.devlog.article.presentation.article.navigation.navigateArticle
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.my_keywords_select.navigation.myKeywordSelectNavGraph
import com.devlog.article.presentation.my_keywords_select.navigation.myKeywordSelectNavigationCompensation
import com.devlog.article.presentation.question.navigateQuestion
import com.devlog.article.presentation.question.questionNavGraph
import com.devlog.article.presentation.question_compensation.navigateQuestionCompensation
import com.devlog.article.presentation.question_compensation.questionCompensationNavGraph
import com.devlog.article.presentation.question_detail.navigateQuestionDetail
import com.devlog.article.presentation.question_detail.questionDetailNavGraph
import com.devlog.article.presentation.sign_in.SignInActivity
import com.devlog.article.presentation.splash.navigation.SplashNCompensation
import com.devlog.article.presentation.splash.navigation.splashNavGraph
import com.devlog.article.presentation.splash.navigation.splashNavigationCompensation
import com.devlog.article.presentation.ui.theme.BottomNavigationBar
import com.devlog.article.utility.UtilManager.keywordCheck
import com.devlog.article.utility.UtilManager.signInCheck
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 승인되었을 때 실행할 코드

            } else {

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission()
        }
        //getArticleData()
        startWebViewHandler()
        //composeStartActivity(this)
        Log.e("polris", "composeStartActivity")
        val title = intent.getStringExtra("title")
        Log.d("polaris",title.toString())

        setContentView(ComposeView(this).apply {


            setContent {

                val navController = rememberNavController()

                // 현재 화면이 `home`이나 `profile`일 때만 바텀 네비게이션을 표시하기 위한 상태
                val showBottomBar = remember { mutableStateOf(false) }

                val receiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        if (resultCode == Activity.RESULT_OK) {
                            val data = resultData?.getSerializable("data") as? Map<String, Data>
                            data?.let {
                                // 수신된 데이터를 처리 (예: Activity UI 업데이트 등)
                                val getApiKeywordList = listOf(0, 12, 10, 3, 9, 4, 5, 6, 7, 8)

                                val totalArticles = ArrayList<ArticleTabState>()
                                val sortedMap: Map<String, Data> = data.toList().sortedBy {
                                    val index = getApiKeywordList.indexOf(it.first.toInt())

                                    // -1이 나오는 경우, 정렬 우선순위를 마지막으로 보내거나 다른 처리를 할 수 있음
                                    if (index != -1) index else Int.MAX_VALUE
                                }.toMap(LinkedHashMap())


                                sortedMap.forEach { (key, date) ->



                                    totalArticles.add(
                                        ArticleTabState(
                                            date.articles,
                                            key.toInt(),
                                            date.maxPage
                                        )
                                    )

                                }
                                viewModel.articleArray.value = totalArticles

                                navController.navigateArticle()
                            }
                        } else {
                            val error = resultData?.getString("error")
                            println("Received error: $error")
                        }
                    }
                }

                // 현재 화면의 경로가 바뀔 때마다 상태 업데이트
                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        showBottomBar.value = destination.route in listOf("article", "question")
                    }
                }

                Scaffold(
                    bottomBar ={ BottomNavigationBar(navController = navController, showBottomBar = showBottomBar) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = SplashNCompensation.route,
                        Modifier.padding(innerPadding)
                    ) {

                        navigation(startDestination = articleRoute.route, route = MainRoute.route) {

                            articleNavGraph(viewModel)
                            questionNavGraph(onQuestionClick = { navController.navigateQuestionDetail() })
                        }

                        questionDetailNavGraph(onQuestionComplete = { navController.navigateQuestionCompensation()})

                        questionCompensationNavGraph(onComplete = { navController.navigateQuestion() })
                        splashNavGraph(resultReceiver = receiver, loginCheck = {

                        } , keywordCheck = {
                            navController.myKeywordSelectNavigationCompensation()

                        })
                        myKeywordSelectNavGraph(onComplete = {navController.splashNavigationCompensation()})
                    }
                }


            }
        })


    }



    private fun askNotificationPermission() {
        // Android 13(API 33) 이상에서만 권한 확인 및 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 권한 상태 확인
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // 권한이 이미 승인되어 있음
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청 이유를 사용자에게 설명해야 함
                // 사용자에게 권한 요청의 필요성을 설명하는 UI를 표시한 후 requestPermissionLauncher 호출
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // 권한 요청 실행
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Android 13 미만의 경우, 별도의 권한 요청 없이 알림을 보낼 수 있음
            // 필요 시 바로 알림을 발송하거나 다른 로직을 수행할 수 있습니다.
        }
    }

    fun articleDetails(title: String, url: String) {
        MixPanelManager.articleClick(title)
        //viewModel.userViewArticleId.add(article._id)
        val intent = Intent(this, ArticleWebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun startWebViewHandler() {
        if (intent.getStringExtra("title")?.isNotEmpty() == true && intent.getStringExtra("url")?.isNotEmpty() == true) {
            articleDetails(intent.getStringExtra("title")!!, intent.getStringExtra("url")!!)
        }
    }


}


