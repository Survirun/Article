package com.devlog.article.presentation.article


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
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
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.databinding.FragmentArticleBinding
import com.devlog.article.presentation.article.adapter.ArticleAdapter
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.bookmark.BookmarkSharedPreferencesHelper
import com.devlog.article.utility.shareLink
import jp.wasabeef.glide.transformations.BlurTransformation


class ArticleListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var binding: FragmentArticleBinding
    lateinit var articleResponse: ArticleResponse
    var articleAdapter = ArticleAdapter()
    val articles = ArrayList<ArticleEntity>()
    lateinit var viewModel: ArticleListViewModel
    lateinit var mixpanel: MixPanelManager
    lateinit var bookmarkSharedPreferencesHelper: BookmarkSharedPreferencesHelper
    val pageMargin by lazy {
        resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
    }
    val pageOffset by lazy {
        resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
    }
    var userViewArticleId = arrayListOf<String>()
    var userBookmarkArticleId = arrayListOf<String>()
    var userShareArticleId = arrayListOf<String>()
    lateinit var userPreference: UserPreference
    var page = 1
    var pageChangePoint = 15
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        viewModel = ArticleListViewModel()
        bookmarkSharedPreferencesHelper = BookmarkSharedPreferencesHelper(requireContext())
        userPreference = UserPreference.getInstance(requireContext())
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        userViewArticleId.forEach {
                            viewArticleLogResponseList.add(ArticleLogResponse(it, "click"))
                        }
                        userShareArticleId.forEach {
                            shareArticleLogResponseList.add(ArticleLogResponse(it, "share"))
                        }
                        userBookmarkArticleId.forEach {
                            bookmarArticleLogResponseList.add(ArticleLogResponse(it, "bookmark"))
                        }

                        if (userViewArticleId.size != 0) {
                            viewModel.postArticleLog(viewArticleLogResponseList)
                        }
                        if (userShareArticleId.size != 0) {
                            viewModel.postArticleLog(shareArticleLogResponseList)
                        }
                        if (userBookmarkArticleId.size != 0) {
                            viewModel.postArticleLog(bookmarArticleLogResponseList)
                        }
                    }

                    else -> {}
                }
            }
        })
        processArticleResponse()
        viewPagerInit()
        setBackgroundImage()
        viewModel.test = {
            userPreference.userName = ""
            userPreference.userUid = ""
            userPreference.userSignInCheck = false
            userPreference.userKeywordCheck = false
            Toast.makeText(context, "앱 계정이 삭제 되었습니다", Toast.LENGTH_SHORT).show()
            finishAffinity(requireActivity())

        }
        viewModel.test1 = {
            Toast.makeText(context, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }

        binding.imageView.setOnClickListener {
            val dlg: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            dlg.setTitle("계정을 삭제하시겠습니까?") //제목

            dlg.setMessage("계정을 삭제하시겠습니까?") // 메시지

            dlg.setPositiveButton(
                "확인",
                DialogInterface.OnClickListener { dialog, which -> //토스트 메시지
                    viewModel.deleteUser()
                    Toast.makeText(context, "삭제중입니다 조금만 기달려주세요", Toast.LENGTH_SHORT).show()
                })
            dlg.show()

        }

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
                    Log.d("test", position.toString())
                    if(position % pageChangePoint == 0 && position != 0) addArticles()
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

    fun addArticles(){
        page += 1
        pageChangePoint += 20
        viewModel.getArticle(userPreference.getUserPagePassed(), page)
        viewModel.succeed = {
            Log.d("test", "오예")
            viewModel.article.data.articles.forEach{
                articles.add(
                    ArticleEntity(
                        title = it.title,
                        text = it.snippet!!,
                        image = it.thumbnail!!,
                        url = it.link,
                        articleId = it._id
                    ))
            }
            articleAdapter.notifyDataSetChanged()
        }
    }

    fun updateLayoutView() {
        viewModel.getArticle(userPreference.getUserPagePassed(), 1)
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
        var list = userPreference.getUserPagePassed()
        articleResponse.data.articles.forEach {
            list.add(it._id)
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
        userPreference.setUserPagePassed(list)
        adapterInit()
    }

    fun adapterInit() {

        articleAdapter.setProductList(
            productList = articles,
            productItemClickListener = { articleDetails(it) },
            articleShareClickListener = { shareArticle(it) },
            articleBookmarkClickListener = { bookmarkArticle(it) },
            articleReportClickListener = { reportArticle(it) },
            isAdmin()
        )
    }

    fun articleDetails(articleEntity: ArticleEntity) {
        MixPanelManager.articleClick(articleEntity.title)
        userViewArticleId.add(articleEntity.articleId)
        val intent = Intent(requireContext(), ArticleWebViewActivity::class.java)
        intent.putExtra("url", articleEntity.url)
        intent.putExtra("title", articleEntity.title)
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
        bookmarkSharedPreferencesHelper.addArticle(articleEntity)
    }

    val viewArticleLogResponseList = arrayListOf<ArticleLogResponse>()
    val shareArticleLogResponseList = arrayListOf<ArticleLogResponse>()
    val bookmarArticleLogResponseList = arrayListOf<ArticleLogResponse>()

    fun reportArticle(articleId: String) {
        viewModel.postReport(articleId)
        viewModel.reportSucceed = {
            Log.e("test", "성공")
        }
        viewModel.reportFailed = {
            Log.e("test", "실패")
        }
    }

    fun isAdmin(): Int {
        return if (userPreference.userPermission == "admin") View.VISIBLE else View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()

    }

}
