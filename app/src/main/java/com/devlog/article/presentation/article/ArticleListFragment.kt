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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.mixpanel.Mixpanel
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
    lateinit var mixpanel: MixPanelManager
    val pageMargin by lazy {
        resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
    }
    val pageOffset by lazy {
        resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
    }
    var userViewArticleId = arrayListOf<String>()
    var userBookmarkArticleId = arrayListOf<String>()
    var userShareArticleId=arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        viewModel = ArticleListViewModel()

        lifecycle.addObserver(object: LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        userViewArticleId.forEach {
                            viewArticleLogResponseList.add(ArticleLogResponse(it,"click"))
                        }
                        userShareArticleId.forEach {
                            shareArticleLogResponseList.add(ArticleLogResponse(it,"share"))
                        }
                        userBookmarkArticleId.forEach {
                            bookmarArticleLogResponseList.add(ArticleLogResponse(it,"bookmark"))
                        }

                        if (userViewArticleId.size!=0){
                            viewModel.postArticleLog(viewArticleLogResponseList)
                        }
                        if (userShareArticleId.size!=0) {
                            viewModel.postArticleLog(shareArticleLogResponseList)
                        }
                        if (userBookmarkArticleId.size!=0 ){
                            viewModel.postArticleLog(bookmarArticleLogResponseList)
                        }
                    }

                    else -> { }
                }
            }
        })
        processArticleResponse()

        viewPagerInit()
        setBackgroundImage()


        binding.swipeLayout.setOnRefreshListener(this)

        return binding.root
    }

    override fun onRefresh() {
        binding.viewPager.isUserInputEnabled = false
        updateLayoutView()
    }

    fun setBackgroundImage() {
        binding.backgroundImage.setColorFilter(
            Color.parseColor("#BDBDBD"),
            PorterDuff.Mode.MULTIPLY
        )

    }

    fun viewPagerInit() {
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
            binding.swipeLayout.isRefreshing = false
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
                    url = it.link,
                    articleId = it._id
                )
            )
        }
        adapterInit()
    }

    fun adapterInit() {

        articleAdapter.setProductList(
            productList = articles,
            productItemClickListener = { articleDetails(it) },
            articleShareClickListener = { shareArticle(it) },
            articleBookmarkClickListener = { bookmarkArticle(it) }
        )
    }

    fun articleDetails(articleEntity: ArticleEntity) {
        MixPanelManager.articleClick(articleEntity.title)
        userViewArticleId.add(articleEntity.articleId)
        val intent = Intent(requireContext(), ArticleWebViewActivity::class.java)
        intent.putExtra("url", articleEntity.url)
        startActivity(intent)
    }

    fun shareArticle(articleEntity: ArticleEntity) {
        MixPanelManager.articleShare(articleEntity.title)
        userShareArticleId.add(articleEntity.articleId)
        requireActivity().shareLink(articleEntity.url)
    }

    fun bookmarkArticle(articleEntity: ArticleEntity) {
        MixPanelManager.articleBookmark(articleEntity.title)
        userBookmarkArticleId.add(articleEntity.articleId)
        viewModel.postBookmark(articleEntity.articleId)
    }
    val viewArticleLogResponseList = arrayListOf<ArticleLogResponse>()
    val shareArticleLogResponseList = arrayListOf<ArticleLogResponse>()
    val bookmarArticleLogResponseList = arrayListOf<ArticleLogResponse>()
    override fun onStop() {
        super.onStop()

    }


}
