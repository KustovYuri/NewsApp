package com.farma.newsapp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BuildTypeModule {
    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}