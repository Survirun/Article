package com.devlog.article.presentation.main

import android.app.Person
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.devlog.article.ProfileFragment
import com.devlog.article.R
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.databinding.ActivityMainBinding
import com.devlog.article.presentation.article.ArticleListFragment
import com.devlog.article.presentation.bookmark.BookmarkFragment
import com.devlog.article.presentation.splash.SplashViewMode
import com.devlog.article.presentation.work_face.WorkFaceFragment


class MainActivity() : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    var articleListFragment=ArticleListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleListFragment.articleResponse = intent.getSerializableExtra("article") as ArticleResponse

        supportFragmentManager.beginTransaction().add(R.id.containers, articleListFragment).commit()
        binding.bottomNavigationview.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    replaceFragment(articleListFragment)
                    true
                }
                R.id.bookmark -> {
                    replaceFragment(BookmarkFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(WorkFaceFragment())
                    true
                }
                else -> false
            }

        }

        binding.bottomNavigationview.menu.forEach { TooltipCompat.setTooltipText(findViewById(it.itemId), null) }
    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.containers, fragment).commit()
    }

}