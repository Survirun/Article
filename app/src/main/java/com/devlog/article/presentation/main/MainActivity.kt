package com.devlog.article.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.databinding.ActivityMainBinding
import com.devlog.article.presentation.article.ArticleFragment
import com.devlog.article.presentation.article.ArticleTabState
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


class MainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var articleFragment = ArticleFragment()
    lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = UserPreference.getInstance(this)


        getArticleData()
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
        val totalArticles = ArrayList<ArticleTabState>()
        val firstKey = if (userPreference.userSignInCheck) "article" else "article_common"
        val keywordList = mapOf(
            firstKey to Common,
            "article_dev_common" to DEVCOMMON,
            "article_it_news" to ITNews,
            "article_android_development" to androidDevelopment,
            "article_ios" to iOSDevelopment,
            "article_web_development" to WebDevelopment,
            "article_server_development" to serverDevelopment,
            "article_ai_development" to AIDevelopment,
            "article_ui_ux_design" to UIUXDesign,
            "article_pm" to PM
        )
        for (keyword in keywordList) {
            val articleResponse = intent.getSerializableExtra(keyword.key) as ArticleResponse
//            val newArticles = ArrayList<ArticleEntity>()
//            articleResponse.data.articles.forEach {
//                if (it.date == null) {
//                    it.date = ""
//                }
//                if (it.snippet == null) {
//                    it.snippet = ""
//                }
//                newArticles.add(
//                    ArticleEntity(
//                        title = it.title,
//                        text = it.snippet!!,
//                        image = it.thumbnail!!,
//                        url = it.link,
//                        articleId = it._id,
//                        type = it.type
//                    )
//                )
//            }
            val newArticles = articleResponse.data.articles as ArrayList<Article>
            totalArticles.add(
                ArticleTabState(
                    newArticles,
                    keyword.value,
                    articleResponse.data.maxPage
                )
            )
        }
        articleFragment.articleArray = totalArticles
    }

}