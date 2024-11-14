package com.devlog.article.presentation.my_keywords_select.state

sealed class MyKeywordSelectApiState {
    object keywordLoading : MyKeywordSelectApiState()
    object postKeywordSuccess : MyKeywordSelectApiState()
    object postKeywordFail : MyKeywordSelectApiState()
}