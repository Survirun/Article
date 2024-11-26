package com.devlog.article.presentation.ui.theme


import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.devlog.article.R
import com.devlog.feature_article_list.navigation.navigateArticle
import com.devlog.question_list.navigateQuestion


@Composable
fun HeaderView() {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(44.dp)
    ) {

    }
}

@Composable
fun BaseColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            content()
        }
    }

}

@Composable
fun BaseBox(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(horizontal = 24.dp)
    ) {
        content()
    }
}


@Composable
fun LineView() {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(1.dp)
            .background(Gray10)
    ) {

    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    showBottomBar: State<Boolean>
) {
    if (showBottomBar.value) {
        BottomNavigation(backgroundColor = Color.White) {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Home") },
                selected = navController.currentDestination?.route == "home",
                onClick = {
                    navController.navigateArticle()
                }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                label = { Text("Profile") },
                selected = navController.currentDestination?.route == "question",
                onClick = {
                    navController.navigateQuestion()
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun preView() {

}

@Composable
fun progressBar() {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Gray10)

    ) {

        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.folphin_loading_dialog).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)

        )


    }
}