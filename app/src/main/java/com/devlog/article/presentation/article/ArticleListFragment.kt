package com.devlog.article.presentation.article

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.databinding.FragmentArticleBinding
import com.devlog.article.presentation.article.adapter.ArticleAdapter
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.utility.shareLink
import jp.wasabeef.glide.transformations.BlurTransformation


class ArticleListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var binding: FragmentArticleBinding
    lateinit var articleResponse: ArticleResponse
    var articleAdapter = ArticleAdapter()
    val articles = ArrayList<ArticleEntity>()
    lateinit var viewModel: ArticleListViewModel

    val pageMargin by lazy {
        resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
    }
    val pageOffset by lazy {
        resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        viewModel = ArticleListViewModel()

        processArticleResponse()
        //viewModel.postArticleLog(arrayListOf<ArticleLogResponse>( ArticleLogResponse(articleResponse.data[0]._id,"click"),ArticleLogResponse(articleResponse.data[1]._id,"click")))
        viewPagerInit()
        setBackgroundImage()


        return binding.root
    }

    override fun onRefresh() {
        binding.viewPager.isUserInputEnabled = false
        updateLayoutView()
    }
    fun setBackgroundImage(){
        binding.backgroundImage.setColorFilter(
            Color.parseColor("#BDBDBD"),
            PorterDuff.Mode.MULTIPLY
        )

    }
    fun viewPagerInit(){
        binding.viewPager.run {
            adapter = articleAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 3

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val image = articleAdapter.getImage(position)

                    Glide.with(this@ArticleListFragment)
                        .asBitmap()
                        .load(image)
//                        .transition(withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                        .into(binding.backgroundImage)
                }
            })

            setPageTransformer { page, position ->
                val myOffset = position * -(2 * pageOffset + pageMargin)
                if (position < -1) {
                    page.translationX = -myOffset
                } else if (position <= 1) {
//                    val scaleFactor = 0.7f.coerceAtLeast(1 - abs(position - 0.14285715f))
                    page.translationX = myOffset
//                    page.alpha = scaleFactor
                } else {
//                    page.alpha = 0f
                    page.translationX = myOffset
                }
            }
        }
    }

    fun updateLayoutView() {
        viewModel.getArticle()
        viewModel.succeed = {
            articles.clear()
            articleResponse = viewModel.article
            processArticleResponse()
            binding.viewPager.isUserInputEnabled = true
            binding.viewPager.setCurrentItem(0, false)
        }
    }

    private fun processArticleResponse() {
         articleResponse.data.forEach {
            if (it.data == null) {
                it.data = ""
            }
            if (it.snippet == null) {
                it.snippet = ""
            }
            articles.add(
                ArticleEntity(
                    title = it.title,
                    text = it.snippet!!,
                    image = it.thumbnail!!,
                    url = it.link
                )
            )
        }
        adapterInit()
    }

    fun adapterInit() {

        articleAdapter.setProductList(articles, {
            val intent = Intent(requireContext(), ArticleWebViewActivity::class.java)
            intent.putExtra("url", it)
            startActivity(intent)
        }, {
            requireActivity().shareLink(it)
        })
    }


}