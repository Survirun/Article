package com.devlog.article.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.devlog.article.R
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.databinding.ActivityMainBinding
import com.devlog.article.presentation.article.ArticleFragment
import com.devlog.article.presentation.bookmark.BookmarkFragment


class MainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var articleFragment = ArticleFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

    private fun getArticleData(){
        val articles = ArrayList<ArticleResponse>()
        val keywordList = listOf("article", "article_it_equipment", "article_it_news", "article_android_development", "article_ios", "article_web_development", "article_server_development", "article_ai_development", "article_ui_ux_design", "article_pm")
        for(keyword in keywordList){
            articles.add(intent.getSerializableExtra(keyword) as ArticleResponse)
        }
        articleFragment.articleArray = articles
    }

}