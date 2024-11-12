package com.devlog.article.presentation.my_keywords_select

import com.devlog.article.data.response.Data
import com.devlog.article.presentation.splash.SplashApiState

sealed class MyKeywordSelectApiState {
    object keywordLoading : MyKeywordSelectApiState()
    object postKeywordSuccess : MyKeywordSelectApiState()
    object postKeywordFail : MyKeywordSelectApiState()
}