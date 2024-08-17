package com.devlog.article.data.request

import java.util.ArrayList

data class ArticleKeywordRequest(val keyword : Int, val page: Int, val passed: ArrayList<String>)