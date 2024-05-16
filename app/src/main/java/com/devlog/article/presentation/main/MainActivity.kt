package com.devlog.article.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.databinding.ActivityMainBinding
import com.devlog.article.presentation.article.ArticleFragment
import com.devlog.article.presentation.article.ArticleTabState
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
        val totalArticles = ArrayList<ArticleTabState>()
        val keywordList = mapOf<String, Int>("article" to 0, "article_it_equipment" to 2, "article_it_news" to 10, "article_android_development" to 3, "article_ios" to 9, "article_web_development" to 5, "article_server_development" to 4, "article_ai_development" to 6, "article_ui_ux_design" to 7, "article_pm" to 8)
        for(keyword in keywordList){
            val articleResponse = intent.getSerializableExtra(keyword.key) as ArticleResponse
            val newArticles = ArrayList<ArticleEntity>()
            articleResponse.data.articles.forEach {
                if (it.data == null) {
                    it.data = ""
                }
                if (it.snippet == null) {
                    it.snippet = ""
                }
                newArticles.add(
                    ArticleEntity(
                        title = it.title,
                        text = it.snippet!!,
                        image = it.thumbnail!!,
                        url = it.link,
                        articleId = it._id
                    )
                )
            }
            totalArticles.add(ArticleTabState(newArticles, keyword.value, articleResponse.data.maxPage))
        }
        articleFragment.articleArray = totalArticles
    }

}