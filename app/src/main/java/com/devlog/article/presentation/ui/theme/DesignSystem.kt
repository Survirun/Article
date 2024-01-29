package com.devlog.article.presentation.ui.theme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    Column(modifier = Modifier.fillMaxSize(1f).background( MaterialTheme.colorScheme.background)) {
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
    Box(modifier = Modifier.fillMaxSize(1f).padding(horizontal = 24.dp)) {
        content()
    }
}





@Composable
fun LineView() {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(1.dp)
            .background(Gray80)
    ) {

    }
}

@Composable
fun MainButton() {

}

@Preview(showBackground = true)
@Composable
fun preView() {

}