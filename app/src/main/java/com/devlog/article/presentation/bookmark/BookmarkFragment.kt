package com.devlog.article.presentation.bookmark

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.devlog.article.R
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.presentation.article_webview.ArticleWebViewActivity
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.article.presentation.ui.theme.HeaderView


class BookmarkFragment : Fragment() {
    lateinit var articleList: MutableState<MutableList<ArticleEntity>>
    private var viewModel = BookmarkViewModel()
    lateinit var bookmarkSharedPreferencesHelper: BookmarkSharedPreferencesHelper
    lateinit var deleteItem: ArticleEntity

    lateinit var showDialog: MutableState<Boolean>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookmarkSharedPreferencesHelper = BookmarkSharedPreferencesHelper(requireContext())
        return ComposeView(requireContext()).apply {

            setContent {
                initData()
               // articleList.value = bookmarkSharedPreferencesHelper.readFromSharedPreferences()
                ArticleTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            HeaderView()
                            Title()
                            if (articleList.value.isNotEmpty()) {
                                BookmarkList()

                            } else {
                                bookMakerEmptView()
                            }
                        }

                    }

                }
            }
        }
    }

    @Composable
    fun initData() {

        arrayListOf<ArticleEntity>()

        articleList = rememberSaveable { mutableStateOf(mutableListOf<ArticleEntity>()) }
        showDialog = rememberSaveable { mutableStateOf(false) }

    }
    @Composable
    fun Title() {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 16.dp, top = 48.dp)) {
            Text(text = "북마크 한 아티클", fontWeight = FontWeight(600), fontSize = 26.sp)
        }
    }



    fun articleDetails(article: ArticleEntity) {
        val intent = Intent(context, ArticleWebViewActivity::class.java)
        intent.putExtra("title", article.title)
        intent.putExtra("url", article.url)
        startActivity(requireContext(), intent, null)
    }

    fun deleteArticle(articleEntity: ArticleEntity) {
        viewModel.postBookmark(articleEntity)
        showDialog.value = false
        articleList.value = articleList.value.filter { it != articleEntity }.toMutableList()
      //  bookmarkSharedPreferencesHelper.saveToSharedPreferences(articleList.value)

    }


    @Composable
    fun BookmarkList() {
        DeleteDialog()
        LazyColumn {
            items(articleList.value) {
                BookmarkItem(it)
            }
        }
    }

    @Composable
    fun BookmarkItem(article: ArticleEntity) {
        Row(
            modifier = Modifier
                .clickable(onClick = { articleDetails(article) })
                .background(Color.White)
                .fillMaxWidth()
                .padding(16.dp),
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
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(0.9f)
            ) {
                ItemText(article.title, Color.Black)
            }
            IconButton(
                onClick = {
                    deleteItem = article
                    showDialog.value = true

                },
                modifier = Modifier.size(32.dp),
            ) {
                Image(
                    painterResource(R.drawable.bookmarkadded),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier,
                )
            }
        }
    }

    @Composable
    fun ItemText(text: String, color: Color) {
        Text(
            text,
            maxLines =2 ,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = color,
            modifier = Modifier.fillMaxWidth(1f)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }

    @Composable
    fun DeleteDialog() {
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "정말 삭제?") },
                text = { Text(text = "레알루다가?") },
                confirmButton = {
                    Button(
                        onClick = {
                            deleteArticle(deleteItem)

                        }) {
                        Text(
                            text = "확인"
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog.value = false }) {
                        Text(
                            text = "취소"
                        )
                    }
                }
            )
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        initData()
        var image =
            "https://content.surfit.io/thumbs/image/wdBn3/oRnWp/19788758706604cf43e9e4e.png/cover-center-1x.webp"
        var articleEntity = ArticleEntity(
            "제목",
            "설명",
            image,
            url = "https://content.surfit.io/thumbs/image/wdBn3/oRnWp/19788758706604cf43e9e4e.png/cover-center-1x.webp",
            "25",
            0
        )

        for (i in 0..10) {
            articleList.value.add(articleEntity)
        }
        ArticleTheme {

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    LazyColumn {
                        items(articleList.value) {
                            BookmarkItem(it)
                        }
                    }
                }

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewItem() {
        var image =
            "https://content.surfit.io/thumbs/image/wdBn3/oRnWp/19788758706604cf43e9e4e.png/cover-center-1x.webp"
        var articleEntity = ArticleEntity(
            "제목",
            "설명",
            image,
            url = "https://content.surfit.io/thumbs/image/wdBn3/oRnWp/19788758706604cf43e9e4e.png/cover-center-1x.webp",
            "25",
            0
        )

        BookmarkItem(articleEntity)
    }

    @Preview(showBackground = true)
    @Composable
    fun bookMakerEmptView() {
        Column {
            Image(
                painter = painterResource(id = R.drawable.dolphin_ascii_art),
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
            )
            Column(Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "현재 북마크 상태의 아티클이 없어요 \uD83D\uDE2D", Modifier.padding(top = 12.dp))
            }


        }

    }

}