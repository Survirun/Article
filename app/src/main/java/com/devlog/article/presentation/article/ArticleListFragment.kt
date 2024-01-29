package com.devlog.article.presentation.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.devlog.article.R
import com.devlog.article.databinding.FragmentArticleBinding
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.presentation.article.adapter.ArticleAdapter
import com.devlog.article.presentation.article.deetail.DetailActivity
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import kotlin.math.abs


class ArticleListFragment : Fragment() {
    lateinit var binding :FragmentArticleBinding
    var articleAdapter =ArticleAdapter()
    val articles = ArrayList<ArticleEntity>()
    lateinit var viewModel: ArticleListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentArticleBinding.inflate(layoutInflater)
        viewModel=ArticleListViewModel()
        viewModel.getArticle()
        viewModel.succeed={
            viewModel.article.forEach{
                if (it.data==null){
                    it.data=""

                }
                if (it.snippet==null){
                    it.snippet=""
                }
                articles.add(ArticleEntity(title=it.title,text= it.snippet!!,image= it.thumbnail!!, url = it.link))

            }
            Log.e("asdfaadfadfa",articles.size.toString())
            adapterInit()
        }
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
    fun dataInit(){
//        articles.add(ArticleEntity("제목1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", R.drawable.test))
//        articles.add(ArticleEntity("제목2", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", R.drawable.test))
//        articles.add(ArticleEntity("제목3", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", R.drawable.test))
//        articles.add(ArticleEntity("제목4", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", R.drawable.test))
//        articles.add(ArticleEntity("제목5", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", R.drawable.test))

    }
    fun adapterInit(){
        articleAdapter.context=requireActivity()
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