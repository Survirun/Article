package com.devlog.article.presentation.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.data.response.Data
import com.devlog.article.databinding.ActivityMainBinding
import com.devlog.article.presentation.article.ArticleFragment
import com.devlog.article.presentation.article.ArticleListViewModel
import com.devlog.article.presentation.article.ArticleTabState
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.bookmark.BookmarkFragment
import com.devlog.article.presentation.my_keywords_select.AIDevelopment
import com.devlog.article.presentation.my_keywords_select.Common
import com.devlog.article.presentation.my_keywords_select.DEVCOMMON
import com.devlog.article.presentation.my_keywords_select.ITNews
import com.devlog.article.presentation.my_keywords_select.PM
import com.devlog.article.presentation.my_keywords_select.UIUXDesign
import com.devlog.article.presentation.my_keywords_select.WebDevelopment
import com.devlog.article.presentation.my_keywords_select.androidDevelopment
import com.devlog.article.presentation.my_keywords_select.iOSDevelopment
import com.devlog.article.presentation.my_keywords_select.serverDevelopment
import com.devlog.article.utility.UtilManager.toJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var articleFragment = ArticleFragment()
    lateinit var userPreference: UserPreference

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 승인되었을 때 실행할 코드

            } else {

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = UserPreference.getInstance(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission()
        }
        getArticleData()
        startWebViewHandler()


        supportFragmentManager.beginTransaction().add(R.id.containers, articleFragment).commit()
        binding.bottomNavigationview.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(articleFragment)
                    true
                }

                R.id.bookmark -> {
                    replaceFragment(BookmarkFragment())
                    true
                }
//                R.id.profile -> {
//                    replaceFragment(ProfileFragment())
//                    true
//                }
                else -> false
            }

        }

        binding.bottomNavigationview.menu.forEach {
            TooltipCompat.setTooltipText(
                findViewById(it.itemId),
                null
            )
        }
    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.containers, fragment).commit()
    }

    private fun getArticleData() {

        // Bundle에서 map 가져오기
        val bundle = intent.getBundleExtra("article_map")
        val articleMap = bundle?.getSerializable("map") as? Map<String, Data>

        // 로그 출력
        var getApiKeywordList = listOf(0, 12, 10, 3,9, 4, 5, 6, 7, 8)
        Log.d("NextActivity", "Article Map: ${articleMap!!.toJson()}")
        val totalArticles = ArrayList<ArticleTabState>()
        val sortedMap: Map<String, Data> = articleMap.toList().sortedBy {
            val index = getApiKeywordList.indexOf(it.first.toInt())
            Log.d("polaris_index",index.toString())
            // -1이 나오는 경우, 정렬 우선순위를 마지막으로 보내거나 다른 처리를 할 수 있음
            if (index != -1) index else Int.MAX_VALUE
        }.toMap(LinkedHashMap())


        sortedMap.forEach{ (key, date) ->

            Log.d("polaris_key",key.toString())

            totalArticles.add(
                ArticleTabState(
                    date.articles,
                    key.toInt(),
                    date.maxPage
                )
            )

        }

        articleFragment.articleArray = totalArticles
    }

    private fun askNotificationPermission() {
        // Android 13(API 33) 이상에서만 권한 확인 및 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 권한 상태 확인
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
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

    fun articleDetails(title:String,url:String) {
        MixPanelManager.articleClick(title)
        //viewModel.userViewArticleId.add(article._id)
        val intent = Intent(this, ArticleWebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun startWebViewHandler(){
        if ( intent.getStringExtra("title")?.isNotEmpty()== true && intent.getStringExtra("url")?.isNotEmpty() == true){
            articleDetails(intent.getStringExtra("title")!!,intent.getStringExtra("url")!!)
        }
    }


}