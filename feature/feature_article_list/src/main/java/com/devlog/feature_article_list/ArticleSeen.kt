package com.devlog.feature_article_list

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devlog.article.presentation.ui.theme.Gray30
import com.devlog.article.presentation.ui.theme.Gray60
import com.devlog.article.presentation.ui.theme.Gray70
import com.devlog.designsystem.R.font
import com.devlog.feature_article_list.state.ArticleApiState
import com.devlog.feature_article_list.state.ArticleTabState
import com.devlog.model.data.entity.response.Article
import com.devlog.preference.PrefManager
import com.devlog.util.UtilManager.toDotDateFormat
import java.net.URI

@Composable
fun ArticleSeen(articles:ArrayList<ArticleTabState>, onComplete: (title:String,url:String) -> Unit, viewModel: ArticleListViewModel = hiltViewModel()) {
    val context = LocalContext.current // Context를 가져옴
    val activity = context as? Activity // Context를 Activity로 캐스팅
    val apiState by viewModel.apiState.collectAsState()

    when(apiState){
        ArticleApiState.Initialize -> {

        }
        is ArticleApiState.GetArticleSuccess -> {
            val newArticles =  (apiState as  ArticleApiState.GetArticleSuccess).articles
            val uniqueNewArticles = newArticles.filterNot { newArticle ->
                viewModel.currentArticles.value.articles.any { currentArticle ->
                    currentArticle._id == newArticle._id
                }
            }
            val updatedArticles = viewModel.currentArticles.value.articles + uniqueNewArticles

            viewModel.currentArticles.value = viewModel.currentArticles.value.copy(articles = updatedArticles as ArrayList<Article>)
            viewModel.articles[viewModel.tabIndex.value] = viewModel.currentArticles.value.copy(articles = updatedArticles)
            viewModel.stateComplete()
        }
        ArticleApiState.GetArticleFailed -> {

        }
        is ArticleApiState.GetArticleKeywordSuccess->{
            val newArticles =  (apiState as  ArticleApiState.GetArticleKeywordSuccess).articles
            val uniqueNewArticles = newArticles.filterNot { newArticle ->
                viewModel.currentArticles.value.articles.any { currentArticle ->
                    currentArticle._id == newArticle._id
                }
            }
            val updatedArticles = viewModel.currentArticles.value.articles + uniqueNewArticles

            viewModel.currentArticles.value = viewModel.currentArticles.value.copy(articles = updatedArticles as ArrayList<Article>)
            viewModel.articles[viewModel.tabIndex.value] = viewModel.currentArticles.value.copy(articles = updatedArticles)
            viewModel.stateComplete()

        }
        ArticleApiState.GetArticleKeywordFailed->{

        }
        ArticleApiState.PostDeleteAccountSuccess ->{
            PrefManager.userName = ""
            PrefManager.userUid = ""
            PrefManager.userSignInCheck = false
            PrefManager.userKeywordCheck = false
            Toast.makeText(context, "앱 계정이 삭제 되었습니다", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
        ArticleApiState.PostDeleteAccountFail ->{
            Toast.makeText(context, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()

        }


    }

//    viewModel.article.observe(activity as MainActivity) {
//        val newArticles = it
//        val uniqueNewArticles = newArticles.filterNot { newArticle ->
//            viewModel.currentArticles.value.articles.any { currentArticle ->
//                currentArticle._id == newArticle._id
//            }
//        }
//        val updatedArticles =
//            viewModel.currentArticles.value.articles + uniqueNewArticles
//
//        viewModel.currentArticles.value =
//            viewModel.currentArticles.value.copy(articles = updatedArticles as ArrayList<Article>)
//        viewModel.articles[viewModel.tabIndex.value] =
//            viewModel.currentArticles.value.copy(articles = updatedArticles)
//    }




    viewModel.articles = articles

    var showDialog by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {
            Header(onShowDialogChange = { showDialog = it })
            ArticleScreen(
                viewModel(),
                showDialog = showDialog,
                onShowDialogChange = { showDialog = it },
                onComplete = onComplete
                )
        }

    }
}



@Composable
fun Header(onShowDialogChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Row 내 아이템을 양쪽 끝으로 배치
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_launcher_app_logo_foreground),
            tint = Color(0xFF000000),
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f)) // 빈 공간을 채우기 위해 Spacer 사용
        Icon(
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    onShowDialogChange(true)
                },
            painter = painterResource(id = R.drawable.baseline_settings_24),
            tint = Color(0xFF000000),
            contentDescription = null
        )
    }
}

fun articleDetails(viewModel: ArticleListViewModel, article: Article, onComplete: (title:String,url:String) -> Unit,) {
//    MixPanelManager.articleClick(article.title)
     viewModel.userViewArticleId.add(article._id)
     onComplete(article.title,article.link )
//    val intent = Intent(context, ArticleWebViewActivity::class.java)
//    intent.putExtra("url", article.link)
//    intent.putExtra("title", article.title)
//    ContextCompat.startActivity(context, intent, null)

}

@Composable
fun ArticleScreen(
    viewModel: ArticleListViewModel,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onComplete: (title:String,url:String) -> Unit,
) {
    viewModel.currentArticles = remember(viewModel.tabIndex.value) { mutableStateOf(viewModel.articles[viewModel.tabIndex.value]) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onShowDialogChange(false) },
            title = { Text(text = "계정을 삭제 하시겠습니까?") },
            text = { Text("계정이 삭제됩니다.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteUser()
                    onShowDialogChange(false)
                }) {
                    Text("확인")
                }
            }
        )
    }



    Column(modifier = Modifier.fillMaxWidth()) {
        val context = LocalContext.current
        ArticleTabLayout(viewModel)
        ArticleList(
            viewModel.currentArticles.value!!,
            onClick = { articleDetails(viewModel, it, onComplete= onComplete)},
            loadMore = {
                viewModel.addArticles(it)
                 },
            maxPage = {}
        )
    }
}

@Composable
fun ArticleTabLayout(viewModel: ArticleListViewModel) {
    val tabs =
        listOf(
            "내 관심사",
            "개발 공통",
            "IT 뉴스",
            "Android",
            "iOS",
            "Web",
            "BackEnd",
            "AI",
            "UIUX",
            "기획"
        )
    ScrollableTabRow(
        selectedTabIndex = viewModel.tabIndex.value,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[viewModel.tabIndex.value]),
                color = Color.Black,
                height = 1.5.dp
            )

        },
        divider = {
            Divider(
                color = Gray30,
                thickness = 1.5.dp
            )
        },
        edgePadding = 20.dp,
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = viewModel.tabIndex.value == index
            Tab(
                modifier = Modifier
                    .padding(0.dp)
                    .width(72.dp),
                selected = isSelected,
                onClick = { viewModel.tabIndex.value = index },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color(0xFFA0A0AB)

            ) {
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .padding(0.dp, 12.dp),
                ) {
                    TabText(title)
                }
            }
        }
    }
}


@Composable
fun ArticleList(
    articleList: ArticleTabState,
    onClick: (i: Article) -> Unit,
    loadMore: (state: ArticleTabState) -> Unit,
    maxPage: () -> Unit
) {
    LazyColumn {
        itemsIndexed(articleList.articles, key = { index, item -> item._id }) { idx, item ->
            if (idx >= articleList.articles.size - 1) {
                if (isMaxPage(articleList)) {
                    maxPage()
                } else {
                    LaunchedEffect(articleList.page) {
                        loadMore(articleList)
                    }
                }
            }
            if (item.type == 2) {
                CompanyArticleItem2(item, onClick = { onClick(item) })
            } else {
                ArticleItem(
                    item, onClick = { onClick(item) }
                )
            }
        }
    }
}

@Preview
@Composable
fun ArticleItem(
    article: Article = Article(
        snippet = "서브 제목 입니다",
        date = "2024.10.08",
        thumbnail = "https://th.bing.com/th?q=Linux&w=138&h=138&c=7&o=5&pid=1.7&mkt=ko-KR&cc=KR&setlang=ko&adlt=strict&t=1",

        displayLink = "",
        sitename = "사이트 이름",
        link = "",
        title = "제목입니다",
        cx = 0,
        _id = "",
        weight = 0.0,
        type = 0

    ), onClick: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    val articleListViewModel: ArticleListViewModel? = if (!isPreview) viewModel() else null

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(Color.White)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArticleText(
            article,
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))

        displayImage2(article,isPreview)

        if (articleListViewModel?.isAdmin()==true) {
            reportButton(article._id)
        }
    }

}
@Composable
fun displayImage2(article: Article, isPreview: Boolean) {
    if (isPreview) {
        // 프리뷰 모드에서는 Image와 painterResource 사용
        Image(
            painter = painterResource(id = R.drawable.yozm),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(81.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    } else {
        // 실제 모드에서는 AsyncImage 사용
        AsyncImage(
            model = article.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(81.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}
@Composable
fun CompanyArticleItem2(article: Article, onClick: () -> Unit) {
    val articleListViewModel: ArticleListViewModel = viewModel()
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(Color.White)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        AsyncImage(
            model = article.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.size(8.dp))
        ArticleText(article)
        if (articleListViewModel.isAdmin()) {
            reportButton(article._id)
        }
    }
}

@Preview
@Composable
fun TabText(title: String = "제목") {
    Text(
        title,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(font.font, FontWeight.SemiBold)
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .wrapContentHeight(align = Alignment.CenterVertically)
    )
}

@Preview
@Composable
fun reportButton(articleId: String = "") {
    val isPreview = LocalInspectionMode.current
    val articleListViewModel: ArticleListViewModel? = if (!isPreview) viewModel() else null

    Button(
        onClick = {
            reportArticle(articleListViewModel, articleId)
        },
        modifier = Modifier.wrapContentSize(),
    ) {
        Text("신고")
    }
}

@Composable
fun ArticleText(article: Article, modifier: Modifier = Modifier) {
    val commonTextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(font.font, FontWeight.Medium))

    )


    val uri = URI(article.link)
    val faviconUrl = "https://${uri.host}/favicon.ico"
    val googleFaviconUrl = "http://www.google.com/s2/favicons?domain=${uri.host}"

    var faviconSource by remember { mutableStateOf(faviconUrl) }

    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        TitleText(article.title)
        Spacer(modifier = Modifier.size(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(faviconSource)
                    .listener(
                        onError = { _, _ ->
                            faviconSource = googleFaviconUrl
                        }
                    )
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = article.sitename,
                style = commonTextStyle.copy(color = Gray70),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = article.date?.toDotDateFormat() ?: "",
                style = commonTextStyle.copy(color = Gray60)
            )
        }
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(font.font, FontWeight.Medium)
        ),
        lineHeight = 24.sp,
        modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
    )
}


fun isMaxPage(articleList: ArticleTabState): Boolean {
    return articleList.page == articleList.maxPage
}


fun reportArticle(viewModel: ArticleListViewModel?, articleId: String) {
    viewModel?.run{
        postArticleReport(articleId)

    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {

    val (tabIndex, setTabIndex) = remember { mutableIntStateOf(0) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {
            Header() {}
            Column(modifier = Modifier.fillMaxWidth()) {
                //TabLayout(tabIndex, setTabIndex)
            }
        }
    }

}