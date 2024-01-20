package com.devlog.article.presentation.article.deetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import com.devlog.article.R
import com.devlog.article.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setTransitionName(binding.image,"tran_image")
        ViewCompat.setTransitionName(binding.title,"tran_title")
        ViewCompat.setTransitionName(binding.text,"tran_text")

        val title = intent.getStringExtra("title")
        val text = intent.getStringExtra("text")
        val image = intent.getIntExtra("image", R.drawable.test)
        binding.title.text = title
        binding.text.text = text
        binding.image.setImageResource(image)
    }
}