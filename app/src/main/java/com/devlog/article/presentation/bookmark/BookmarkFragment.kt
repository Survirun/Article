package com.devlog.article.presentation.bookmark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devlog.article.R
import com.devlog.article.databinding.FragmentBookmarkBinding
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity

class BookmarkFragment : Fragment() {
    lateinit var viewModel: BookmarkViewModel
    lateinit var binding: FragmentBookmarkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(layoutInflater)
        binding.test.setOnClickListener {
            startActivity(Intent(requireContext(), Bookmark::class.java))
        }
        return binding.root
    }
}