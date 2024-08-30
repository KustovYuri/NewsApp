package com.farma.newsapp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BuildTypeModule {
    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient{
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .build()
    }
}