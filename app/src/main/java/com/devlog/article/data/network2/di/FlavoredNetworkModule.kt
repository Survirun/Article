package com.devlog.article.data.network2.di

import com.devlog.article.data.network2.ArticleDataSource
import com.devlog.article.data.network2.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNetwork): ArticleDataSource
}
