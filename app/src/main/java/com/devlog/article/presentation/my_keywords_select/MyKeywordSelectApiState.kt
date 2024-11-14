package com.devlog.article.presentation.my_keywords_select

sealed class MyKeywordSelectApiState {
    object keywordLoading : MyKeywordSelectApiState()
    object postKeywordSuccess : MyKeywordSelectApiState()
    object postKeywordFail : MyKeywordSelectApiState()
}