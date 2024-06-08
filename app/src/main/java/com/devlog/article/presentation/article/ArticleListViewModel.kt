package com.devlog.article.presentation.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.ArticleEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.ArticleRepository
import com.devlog.article.data.repository.ArticleRepositoryImpl
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.UserRepository
import com.devlog.article.data.response.Article
import com.devlog.article.data.response.ArticleLogResponse
import com.devlog.article.presentation.my_keywords_select.AIDevelopment
import com.devlog.article.presentation.my_keywords_select.ITEquipment
import com.devlog.article.presentation.my_keywords_select.ITNews
import com.devlog.article.presentation.my_keywords_select.MyInterestsArticle
import com.devlog.article.presentation.my_keywords_select.PM
import com.devlog.article.presentation.my_keywords_select.UIUXDesign
import com.devlog.article.presentation.my_keywords_select.WebDevelopment
import com.devlog.article.presentation.my_keywords_select.androidDevelopment
import com.devlog.article.presentation.my_keywords_select.iOSDevelopment
import com.devlog.article.presentation.my_keywords_select.serverDevelopment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleListViewModel : ViewModel() {
    var userSignCheck =true
    var permission =""
    lateinit var failed: () -> Unit
    lateinit var reportSucceed: () -> Unit
    lateinit var reportFailed: () -> Unit
    lateinit var article: ArrayList<Article>
    lateinit var bookmark: ArrayList<ArticleEntity>

    lateinit var test: () -> Unit
    lateinit var test1: () -> Unit

    var articleTabState =ArticleTabState(ArrayList(),0,0,0)

    private val _articleLiveDataMap = mapOf(
        "my_interests_article" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_it_equipment" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_it_news" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_android_development" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_ios" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_web_development" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_server_development" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_ai_development" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_ui_ux_design" to MutableStateFlow<ArticleTabState>(articleTabState),
        "article_pm" to MutableStateFlow<ArticleTabState>(articleTabState)
    )

    val articleLiveDataMap: Map<String, StateFlow<ArticleTabState>> = _articleLiveDataMap

    fun updateArticles(type: String, articles:ArticleTabState) {
        _articleLiveDataMap[type]?.value = articles

    }

    //type을 넣어 아티클 가져오기 디버깅용
    fun getArticles(type: String): StateFlow<ArticleTabState> {
        return articleLiveDataMap[type]!!
    }

    fun processIntent(intent: ArticleIntent) {
        when (intent) {

            is ArticleIntent.LoadArticles ->{
                if (intent.keyword==MyInterestsArticle){

                    getArticleApi(arrayListOf<String>(),intent.page)
                }else{
                    getArticleKeyword(intent.page,intent.keyword, arrayListOf())
                }
            }
        }
    }
    //키워드 코드를 Map 키로 바꿈
    fun keywordCodeToKeywordMap(num: Int): String{
        val keyword = when (num) {
            MyInterestsArticle -> "my_interests_article"
            ITEquipment -> "article_it_equipment"
            ITNews-> "article_it_news"
            androidDevelopment -> "article_android_development"
            iOSDevelopment -> "article_ios"
            WebDevelopment -> "article_web_development"
            serverDevelopment-> "article_server_development"
            AIDevelopment -> "article_ai_development"
            UIUXDesign -> "article_ui_ux_design"
            PM -> "article_pm"
            else -> ""
        }

        return  keyword
    }
    //탭 순서대로 Map 키로 바꿈
    fun keywordCodeTabToKeywordMap(num: Int): StateFlow<ArticleTabState> {
        val keyword = when (num) {
            0 -> "my_interests_article"
            1 -> "article_it_equipment"
            2 -> "article_it_news"
            3 -> "article_android_development"
            4 -> "article_ios"
            5 -> "article_web_development"
            6 -> "article_server_development"
            7 -> "article_ai_development"
            8 -> "article_ui_ux_design"
            9 -> "article_pm"
            else -> null
        }
        return  articleLiveDataMap[keyword] !!
    }



    fun updateArticle(article:ArrayList<Article>,keyword:String){
        val newArticles = article.map {
            ArticleEntity(
                title = it.title,
                text = it.snippet!!,
                image = it.thumbnail!!,
                url = it.link,
                articleId = it._id,
                type = it.type

            )
        }

        val uniqueNewArticles = newArticles.filterNot { newArticle ->
            articleLiveDataMap[keyword]!!.value.articles.any { currentArticle ->
                currentArticle.articleId == newArticle.articleId
            }
        }
        val updatedArticles =  articleLiveDataMap[keyword]!!.value.articles + uniqueNewArticles
        updateArticles(keyword, articleLiveDataMap[keyword]!!.value.copy(articles = updatedArticles as ArrayList<ArticleEntity>))


    }

    fun getArticleApi(passed: ArrayList<String>, page : Int): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticle(page, passed)
        if (serverCode != null) {
            article = serverCode.data.articles as ArrayList<Article>
            val keyword = keywordCodeToKeywordMap(0)


           updateArticle(article,keyword)

        } else {
            failed()
        }


    }
    fun getArticleKeyword(page: Int, keyword: Int, pass: ArrayList<String>): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )

        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.getArticleKeyword(keyword, page, pass)

        if (serverCode != null) {
            val keyword = keywordCodeToKeywordMap(keyword)
            article = serverCode.data.articles as ArrayList<Article>

            updateArticle(article,keyword)


        } else {
            failed()
        }
    }

    fun postArticleLog(postArticleLogResponse: ArrayList<ArticleLogResponse>): Job =
        viewModelScope.launch {
            val api = ApiService(
                provideProductRetrofit(
                    buildOkHttpClient(),
                    provideGsonConverterFactory()
                )
            )
            val repository: ArticleRepository =
                ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
            val serverCode = repository.postArticleLog(postArticleLogResponse)

        }

    fun postBookmark(articleId: String): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.postBookmark(articleId)
        if (serverCode) {

        } else {
            failed()
        }

    }

    fun postReport(articleId: String): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: ArticleRepository =
            ArticleRepositoryImpl.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.postReport(articleId)
        if (serverCode) {
            reportSucceed()
        } else {
            reportFailed()
        }

    }

    fun deleteUser(): Job = viewModelScope.launch {
        val api = ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: UserRepository =
            DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverCode = repository.deleteUser()
        if (serverCode==200) {
            test()
        } else {
            test1()
        }
    }

}