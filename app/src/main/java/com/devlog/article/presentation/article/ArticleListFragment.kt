package com.devlog.article.presentation.article

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.devlog.article.R
import com.devlog.article.databinding.FragmentArticleBinding
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.presentation.article.adapter.ArticleAdapter
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import kotlin.math.abs


class ArticleListFragment : Fragment() {
    lateinit var binding :FragmentArticleBinding
    lateinit var articleResponse: ArticleResponse
    var articleAdapter =ArticleAdapter()
    var article= listOf<Article>()
    val articles = ArrayList<ArticleEntity>()
    lateinit var viewModel: ArticleListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentArticleBinding.inflate(layoutInflater)
        viewModel=ArticleListViewModel()

        article=articleResponse.data
        article.forEach{
            if (it.data==null){
                it.data=""

            }
            if (it.snippet==null){
                it.snippet=""
            }
            articles.add(ArticleEntity(title=it.title,text= it.snippet!!,image= it.thumbnail!!, url = it.link))

        }
        adapterInit()


        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()


        binding.viewPager.run {
            adapter = articleAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            setPageTransformer { page, position ->
                val myOffset = position * -(2 * pageOffset + pageMargin)
                if (position < -1) {
                    page.translationX = -myOffset
                } else if (position <= 1) {
                    val scaleFactor = 0.7f.coerceAtLeast(1 - abs(position - 0.14285715f))
                    page.translationX = myOffset
                    page.scaleY = scaleFactor
                    page.alpha = scaleFactor
                } else {
                    page.alpha = 0f
                    page.translationX = myOffset
                }
            }
        }




        return binding.root
    }

    fun adapterInit(){

        articleAdapter.setProductList(articles){
//            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                requireActivity(),
//                androidx.core.util.Pair<View, String>(
//                    articleAdapter.articleViewHolder.binding.image,
//                    "tran_image"
//                ),
//                androidx.core.util.Pair<View, String>(
//                    articleAdapter.articleViewHolder.binding.title,
//                    "tran_title"
//                ),
//                androidx.core.util.Pair<View, String>(
//                    articleAdapter.articleViewHolder.binding.text,
//                    "tran_text"
//                )
//            )
//
//            val intent = Intent(requireContext(), DetailActivity::class.java)
//            intent.putExtra("title", it.title)
//            intent.putExtra("text", it.text)
//            intent.putExtra("image", it.image)
//            ContextCompat.startActivity(requireContext(), intent, activityOptions.toBundle())

            val intent = Intent(requireContext(), ArticleWebViewActivity::class.java)
            intent.putExtra("url", it.url)
            startActivity(intent)

        }
    }


}