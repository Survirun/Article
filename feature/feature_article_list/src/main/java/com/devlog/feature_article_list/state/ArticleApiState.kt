package com.devlog.feature_article_list.state

import com.devlog.model.data.entity.response.Article

sealed class ArticleApiState {
    data object Initialize : ArticleApiState()
    data class GetArticleSuccess(val articles: ArrayList<Article>) : ArticleApiState()
    data object GetArticleFailed : ArticleApiState()
    data class GetArticleKeywordSuccess(val articles: ArrayList<Article>) : ArticleApiState()
    data object GetArticleKeywordFailed : ArticleApiState()
    data object PostDeleteAccountSuccess : ArticleApiState()
    data object PostDeleteAccountFail : ArticleApiState()
}