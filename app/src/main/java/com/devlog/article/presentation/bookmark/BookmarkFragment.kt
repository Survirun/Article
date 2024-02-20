package com.devlog.article.presentation.bookmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devlog.article.R

class BookmarkFragment : Fragment() {
    lateinit var viewModel: BookmarkViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel= BookmarkViewModel()
        viewModel.getBookMaker()
        viewModel.succeed={
            Log.d("test", viewModel.article.toString())
        }
        viewModel.failed={

        }
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }
}