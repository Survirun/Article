package com.devlog.article.presentation.bookmark

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
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


var articleList = ArrayList<ArticleEntity>()
private var viewModel = BookmarkViewModel()
lateinit var bookmarkSharedPreferencesHelper: BookmarkSharedPreferencesHelper

class BookmarkFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookmarkSharedPreferencesHelper = BookmarkSharedPreferencesHelper(requireContext())
        return ComposeView(requireContext()).apply {
            articleList = bookmarkSharedPreferencesHelper.readFromSharedPreferences()

            setContent {
                ArticleTheme {
                    // A surface container using the 'background' color from the theme
                    Main(requireContext())
                }
            }
        }
    }
}

@Composable
fun Main(context: Context) {

    fun deleteTodo(id: String) {
        viewModel.postBookmark(id)
        articleList = articleList.filter { it.articleId != id } as ArrayList<ArticleEntity>
        viewModel.succeed = {
            bookmarkSharedPreferencesHelper.saveToSharedPreferences(articleList)
        }
    }

    fun articleDetails(url: String) {
        val intent = Intent(context, ArticleWebViewActivity::class.java)
        intent.putExtra("url", url)
        startActivity(context, intent, null)
    }

    val (showDialog, setShowDialog) =
        rememberSaveable { mutableStateOf(false) }
    var deleteItem by rememberSaveable { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (articleList.isNotEmpty()) {

            BookmarkList(articleList,
                onClick = {
                    articleDetails(it)
                },
                onDelete = {
                    deleteItem = it
                    setShowDialog(true)
                })
            DeleteDialog(showDialog, setShowDialog) {
                deleteTodo(deleteItem)
            }
        } else {
            Text("비어있음")
        }
    }
}

@Composable
fun BookmarkList(
    articleList: ArrayList<ArticleEntity>,
    onClick: (i: String) -> Unit,
    onDelete: (i: String) -> Unit
) {
    LazyColumn {
        items(articleList) {
            BookmarkItem(it,
                onClick = { onClick(it.url) },
                onDelete = { onDelete(it.articleId) })
        }
    }
}

@Composable
fun BookmarkItem(article: ArticleEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(Color.White)
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (article.url.contains("yozm.wishket")) R.drawable.yozm else article.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp, end = 16.dp).fillMaxWidth(0.9f)) {
            ItemText(article.title, Color.Black)
            ItemText(article.text!!, Color(0xFFA0A0AB))
        }
        IconButton(
            onClick = {
                onDelete()
            },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.BookmarkAdded,
                tint = Color(0xFFA0A0AB),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun ItemText(text: String, color: Color) {
    Text(
        text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 14.sp,
        fontFamily = FontFamily(
            Font(R.font.font, FontWeight.Medium)
        ),
        color = color,
        modifier = Modifier.height(24.dp).wrapContentHeight(align = Alignment.CenterVertically)
    )
}

@Composable
fun DeleteDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    if (!showDialog) return
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        title = { Text(text = "정말 삭제?") },
        text = { Text(text = "레알루다가?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    viewModel.succeed = {
                        setShowDialog(false)
                    }
                }) {
                Text(
                    text = "확인"
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { setShowDialog(false) }) {
                Text(
                    text = "취소"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ArticleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            BookmarkList(articleList)
        }
    }
}