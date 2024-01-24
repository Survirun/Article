package com.devlog.article.presentation.article.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devlog.article.R
import com.devlog.article.databinding.ViewholderArticleItemBinding
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.presentation.article.deetail.DetailActivity

class ArticleAdapter() : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    private var articleList:List<ArticleEntity> = listOf()
    private lateinit var articleItemClickListener: (ArticleEntity) -> Unit
    lateinit var articleViewHolder:ArticleViewHolder
    lateinit var context:Activity
    inner class ArticleViewHolder(
         var  binding: ViewholderArticleItemBinding,
        val articleItemClickListener: (ArticleEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(data: ArticleEntity) {
            binding.run {

                title.text = data.title
                text.text = data.text
                image.setImageResource(data.image)
            }
            binding.card.setOnClickListener {
              //  articleItemClickListener(data)

            }
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {

        val view = ViewholderArticleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)



        return ArticleViewHolder(view,articleItemClickListener)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bindViews(articleList[position])
        holder.binding.card.setOnClickListener {
            articleViewHolder=holder
            articleItemClickListener(articleList[position])
//            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                context,
//                androidx.core.util.Pair<View, String>(
//                    holder.binding.image,
//                    "tran_image"
//                ),
//                androidx.core.util.Pair<View, String>(
//                    holder.binding.title,
//                    "tran_title"
//                ),
//                androidx.core.util.Pair<View, String>(
//                    holder.binding.text,
//                    "tran_text"
//                )
//            )
//
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("title", articleList[position].title)
//            intent.putExtra("text", articleList[position].text)
//            intent.putExtra("image", articleList[position].image)
//            ContextCompat.startActivity(context, intent, activityOptions.toBundle())
        }


    }

    override fun getItemCount(): Int = articleList.size

    fun setProductList(productList: List<ArticleEntity>, productItemClickListener: (ArticleEntity) -> Unit={ }){
        this.articleList=productList
        this.articleItemClickListener=productItemClickListener
        notifyDataSetChanged()
    }

}