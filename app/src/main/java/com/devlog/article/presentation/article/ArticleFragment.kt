package com.devlog.article.presentation.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.data.response.ArticleResponse
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme

lateinit var viewModel: ArticleListViewModel
lateinit var userPreference: UserPreference
val articles = mutableStateListOf<ArticleEntity>()
var page = 1
var pageChangePoint = 10
var userViewArticleId = arrayListOf<String>()
var userBookmarkArticleId = arrayListOf<String>()
var userShareArticleId = arrayListOf<String>()
val viewArticleLogResponseList = arrayListOf<ArticleLogResponse>()
val shareArticleLogResponseList = arrayListOf<ArticleLogResponse>()
val bookmarArticleLogResponseList = arrayListOf<ArticleLogResponse>()

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
            TabScreen()
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
fun TabScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("내 관심사", "IT 기기", "IT 소식", "Android", "Web", "BackEnd", "AI", "UIUX", "기획")

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color.Transparent,
            indicator = {},
            divider = {},
            edgePadding = 20.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { TitleText(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color(0xFFA0A0AB)

                )
            }
        }
//        when (tabIndex) {
//            0 -> HomeScreen()
//            1 -> AboutScreen()
//            2 -> SettingsScreen()
//            3 -> MoreScreen()
//            4 -> SomethingScreen()
//            5 -> EverythingScreen()
//        }
    }
}

@Composable
fun TitleText(title: String) {
    Text(
        title,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(R.font.font, FontWeight.SemiBold)
        ),
        modifier = Modifier.height(24.dp).wrapContentHeight(align = Alignment.CenterVertically)
    )
}

@Composable
fun ArticleList(
    articleList: List<ArticleEntity>,
    onClick: (i: ArticleEntity) -> Unit
) {
    LazyColumn {
        itemsIndexed(articleList) { idx, item ->
            if (idx == pageChangePoint) {
                LaunchedEffect(page) {
                    addArticles()
                }
            }
            if (isCompanyArticle(item.url)) {
                CompanyArticleItem(item, onClick = { onClick(item) })
            } else {
                ArticleItem(
                    item, onClick = { onClick(item) }
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
        if (isAdmin()) {
            reportButton(article.articleId)
        }
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
        if (isAdmin()) {
            reportButton(article.articleId)
        }
    }
}

@Composable
fun reportButton(articleId: String) {
    Button(
        onClick = { reportArticle(articleId) },
        modifier = Modifier.wrapContentSize(),
    ) {
        Text("신고")
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

fun addArticles() {
    page += 1
    pageChangePoint += 20
    viewModel.getArticle(userPreference.getUserPagePassed(), page)
    viewModel.succeed = {
        viewModel.article.data.articles.forEach {
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
    }
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

fun isAdmin(): Boolean {
    return userPreference.userPermission == "admin"
}

fun isCompanyArticle(url: String): Boolean {
    return (url.contains("toss.tech"))
}

fun reportArticle(articleId: String) {
    viewModel.postReport(articleId)
    viewModel.reportSucceed = {
        Log.e("test", "성공")
    }
    viewModel.reportFailed = {
        Log.e("test", "실패")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArticleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                header()
                TabScreen()
            }
        }
    }
}