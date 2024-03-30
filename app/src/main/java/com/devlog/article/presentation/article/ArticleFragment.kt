package com.devlog.article.presentation.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme

lateinit var viewModel: ArticleListViewModel
lateinit var userPreference: UserPreference
val articles = ArrayList<ArticleEntity>()
var page = 1
var pageChangePoint = 15
var userViewArticleId = arrayListOf<String>()

class ArticleFragment : Fragment() {
    lateinit var articleResponse: ArticleResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ArticleListViewModel()
        userPreference = UserPreference.getInstance(requireContext())
        processArticleResponse(articleResponse)
        return ComposeView(requireContext()).apply {
            setContent {
                ArticleTheme {
                    Main(requireContext())
                }
            }
        }
    }
}

@Composable
fun Main(context: Context) {

    fun articleDetails(articleEntity: ArticleEntity) {
        MixPanelManager.articleClick(articleEntity.title)
        userViewArticleId.add(articleEntity.articleId)
        val intent = Intent(context, ArticleWebViewActivity::class.java)
        intent.putExtra("url", articleEntity.url)
        intent.putExtra("title", articleEntity.title)
        ContextCompat.startActivity(context, intent, null)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            header()
            ArticleList(articles, onClick = { articleDetails(it) })
        }
    }
}

@Composable
fun header() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = R.drawable.ic_article_24),
            tint = Color(0xFFA0A0AB),
            contentDescription = null
        )
    }
}

@Composable
fun ArticleList(
    articleList: ArrayList<ArticleEntity>,
    onClick: (i: ArticleEntity) -> Unit
) {
    LazyColumn {
        items(articleList) {
            if (isCompanyArticle(it.url)) {
                CompanyArticleItem(it, onClick = { onClick(it) })
            } else {
                ArticleItem(
                    it, onClick = { onClick(it) }
                )
            }
        }
    }
}

@Composable
fun ArticleItem(article: ArticleEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(Color.White)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (article.url.contains("yozm.wishket")) R.drawable.yozm else article.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        ItemText(article.title, 0.dp)
    }

}

@Composable
fun CompanyArticleItem(article: ArticleEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(Color.White)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        AsyncImage(
            model = if (article.url.contains("yozm.wishket")) R.drawable.yozm else article.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(4.dp))
        )
        ItemText(article.title, 12.dp)
    }
}

@Composable
fun ItemText(text: String, paddingTop: Dp) {
    Text(
        text,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(R.font.font, FontWeight.Medium)
        ),
        modifier = Modifier.height(48.dp).wrapContentHeight(align = Alignment.CenterVertically)
            .padding(start = 8.dp, top = paddingTop)
    )
}

fun processArticleResponse(articleResponse: ArticleResponse) {
    val list = userPreference.getUserPagePassed()
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
}

fun isAdmin(): Int {
    return if (userPreference.userPermission == "admin") View.VISIBLE else View.INVISIBLE
}

fun isCompanyArticle(url: String): Boolean {
    return (url.contains("toss.tech"))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArticleTheme {
    }
}