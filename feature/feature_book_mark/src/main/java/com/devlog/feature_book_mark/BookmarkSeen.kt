package com.devlog.feature_book_mark

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.devlog.article.presentation.ui.theme.ArticleTheme
import com.devlog.designsystem.theme.HeaderView
import com.devlog.model.data.entity.response.Article


@Composable
fun BookmarkSeen(viewModel:BookmarkViewModel = hiltViewModel(),  onComplete: (title:String,url:String) -> Unit){
    val context = LocalContext.current

    viewModel.ada().collectAsState(0)


    BookmarkView(onComplete = onComplete)



}
@Composable
fun BookmarkView(viewModel:BookmarkViewModel = hiltViewModel(),  onComplete: (title:String,url:String) -> Unit){
    ArticleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                HeaderView()
                Title()
                if (viewModel.articleList.isNotEmpty()) {
                    BookmarkList(onComplete = onComplete)

                } else {
                    bookMakerEmptView()
                }
            }

        }

    }
}


@Composable
fun Title() {
    Column(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 16.dp, top = 48.dp)) {
        Text(text = "북마크 한 아티클", fontWeight = FontWeight(600), fontSize = 26.sp)
    }
}


@Composable
fun BookmarkList(viewModel: BookmarkViewModel = hiltViewModel(),  onComplete: (title:String,url:String) -> Unit) {
    var showDialog = remember { mutableStateOf(false) }

    var deleteItemItem = remember { mutableStateOf<Article>(Article()) }

    DeleteDialog(showDialog = showDialog,article= deleteItemItem.value )
    LazyColumn {
        items(viewModel.articleList) {
            BookmarkItem(it, onComplete = onComplete, onDeleteItem = {
                deleteItemItem.value = it
                showDialog.value = true
            })


        }
    }
}

@Composable
fun BookmarkItem(article: Article,  onComplete: (title:String,url:String) -> Unit,onDeleteItem:()->Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = {onComplete(article.title,article.link)})
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (article.link.contains("yozm.wishket")) R.drawable.yozm else article.thumbnail,
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

                onDeleteItem()


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
        modifier = Modifier
            .fillMaxWidth(1f)
            .wrapContentHeight(align = Alignment.CenterVertically)
    )
}

@Composable
fun DeleteDialog(viewModel: BookmarkViewModel= hiltViewModel(), showDialog: MutableState<Boolean>, article: Article) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "정말 삭제?") },
            text = { Text(text = "레알루다가?") },
            confirmButton = {
                Button(
                    onClick = {

                        viewModel.postBookmark(article)
                        showDialog.value = false
                        viewModel.articleList =  viewModel.articleList .filter { it != article }.toMutableList()
                        //  bookmarkSharedPreferencesHelper.saveToSharedPreferences(articleList.value)



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