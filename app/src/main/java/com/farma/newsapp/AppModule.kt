package com.farma.newsapp

import android.content.Context
import com.farma.common.AndroidLogcatLogger
import com.farma.common.AppDispatchers
import com.farma.common.Logger
import com.farma.database.NewsDatabase
import com.farma.newsapi.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient?): NewsApi {
        return NewsApi(
            baseUrl = BuildConfig.NEWS_API_BASE_URL,
            apiKey = BuildConfig.NEWS_API_KEY,
            okHttpClient = okHttpClient
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return NewsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    fun provideLogger(): Logger = AndroidLogcatLogger()
}
