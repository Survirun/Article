package com.devlog.article.presentation.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.mixpanel.MixPanelManager
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.my_keywords_select.Common
import com.devlog.article.presentation.ui.theme.ArticleTheme


lateinit var pass: ArrayList<String>
var articles = ArrayList<ArticleTabState>()
var userViewArticleId = arrayListOf<String>()





val LocalViewModel = staticCompositionLocalOf<ArticleListViewModel> { error("MainViewModel not provided") }
class ArticleFragment : Fragment() {
    lateinit var articleArray: ArrayList<ArticleTabState>
    lateinit var viewModel: ArticleListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userPreference = UserPreference.getInstance(requireContext())
        pass = userPreference.getUserPagePassed()


        viewModel = ArticleListViewModel()
        viewModel.userSignCheck = userPreference.userSignInCheck
        viewModel.permission = userPreference.userPermission
        processArticleResponse(articleArray)

        onStateChanged()
        return ComposeView(requireContext()).apply {
            setContent {
                ArticleTheme {
                    Main(viewModel)
                }
            }
        }
    }

    fun onStateChanged(){
        var userBookmarkArticleId = arrayListOf<String>()
        val bookmarArticleLogResponseList = arrayListOf<ArticleLogResponse>()
        val viewArticleLogResponseList = arrayListOf<ArticleLogResponse>()
        val shareArticleLogResponseList = arrayListOf<ArticleLogResponse>()
        var userShareArticleId = arrayListOf<String>()
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


    }
}

@Composable
fun Main(viewModel: ArticleListViewModel) {
    CompositionLocalProvider(LocalViewModel provides viewModel) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column {
                Header()
                ArticleScreen(viewModel)
            }
        }
    }
}

@Composable
fun Header() {
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

fun articleDetails(articleEntity: ArticleEntity,context:Context) {
    MixPanelManager.articleClick(articleEntity.title)
    userViewArticleId.add(articleEntity.articleId)
    val intent = Intent(context, ArticleWebViewActivity::class.java)
    intent.putExtra("url", articleEntity.url)
    intent.putExtra("title", articleEntity.title)
    ContextCompat.startActivity(context, intent, null)
}

@Composable
fun ArticleScreen(viewModel: ArticleListViewModel) {
    val (tabIndex, setTabIndex) = remember { mutableIntStateOf(0) }
    val currentArticles = remember(tabIndex) { mutableStateOf(articles[tabIndex]) }



    fun addArticles(articleTabState: ArticleTabState) {
        articleTabState.page += 1
        if (viewModel.userSignCheck && articleTabState.keyword == Common){
            viewModel.getArticle(
                pass,
                articleTabState.page
            )
        }
        else{
            viewModel.getArticleKeyword(articleTabState.page, articleTabState.keyword, pass)
        }
        viewModel.succeed = {
            val newArticles = viewModel.article.map {
                ArticleEntity(
                    title = it.title,
                    text = it.snippet!!,
                    image = it.thumbnail!!,
                    url = it.link,
                    articleId = it._id,
                    type = it.type

                )
            }
            val uniqueNewArticles = newArticles.filterNot { newArticle ->
                currentArticles.value.articles.any { currentArticle ->
                    currentArticle.articleId == newArticle.articleId
                }
            }
            val updatedArticles = articleTabState.articles + uniqueNewArticles
            currentArticles.value =
                articleTabState.copy(articles = updatedArticles as ArrayList<ArticleEntity>)
            articles[tabIndex] = articleTabState.copy(articles = updatedArticles)
        }
    }

    fun maxPage() {
//        Toast.makeText(context, "끝에 도달", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        val context = LocalContext.current
        TabLayout(tabIndex, setTabIndex)
        ArticleList(
            currentArticles.value,
            onClick = { articleDetails(it,context) },
            loadMore = { addArticles(it) },
            maxPage = { maxPage() }
        )
    }
}

@Composable
fun TabLayout(tabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs =
        listOf(
            if (LocalViewModel.current.userSignCheck) "내 관심사" else "공통",
            "IT 기기",
            "IT 소식",
            "Android",
            "iOS",
            "Web",
            "BackEnd",
            "AI",
            "UIUX",
            "기획"
        )
    ScrollableTabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                color = Color.Black,
                height = 1.dp
            )

        },
        divider = {},
        edgePadding = 20.dp
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = tabIndex == index
            Tab(
                text = { TitleText(title, isSelected) },
                selected = isSelected,
                onClick = { onTabSelected(index) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color(0xFFA0A0AB)

            )
        }
    }
}


@Composable
fun ArticleList(
    articleList: ArticleTabState,
    onClick: (i: ArticleEntity) -> Unit,
    loadMore: (state: ArticleTabState) -> Unit,
    maxPage: () -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
        itemsIndexed(articleList.articles, key = { index, item -> item.articleId }) { idx, item ->
            if (idx >= articleList.articles.size - 1) {
                if (isMaxPage(articleList)) {
                    maxPage()
                } else {
                    LaunchedEffect(articleList.page) {
                        loadMore(articleList)
                    }
                }
            }
            if (item.type==2) {
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
            .padding(vertical = 16.dp),
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
        Spacer(modifier = Modifier.size(8.dp))
        ItemText(article.title, 0.dp)
        if (isAdmin(LocalViewModel.current.permission)) {
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
            .padding(vertical = 16.dp),
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
        Spacer(modifier = Modifier.size(12.dp))
        ItemText(article.title, 12.dp)
        if (isAdmin(LocalViewModel.current.permission)) {
            reportButton(article.articleId)
        }
    }
}

@Composable
fun TitleText(title: String, isSelected: Boolean) {
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
fun reportButton(articleId: String) {
    val viewModel = LocalViewModel.current
    Button(
        onClick = { reportArticle(viewModel,articleId) },
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
        modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
    )
}


fun processArticleResponse(articleArray: ArrayList<ArticleTabState>) {
    val list = pass
    articleArray.forEach { articleTabState ->
        articleTabState.articles.forEach {
            list.add(it.articleId)
        }
    }
    articles = articleArray
}

fun isMaxPage(articleList: ArticleTabState): Boolean {
    return articleList.page == articleList.maxPage
}

fun isAdmin(permission:String): Boolean {
    return permission == "admin"
}



fun reportArticle(viewModel: ArticleListViewModel,articleId: String) {
    viewModel.postReport(articleId)
    viewModel.reportSucceed = {
        Log.e("test", "성공")
    }
    viewModel.reportFailed = {
        Log.e("test", "실패")
    }

}

