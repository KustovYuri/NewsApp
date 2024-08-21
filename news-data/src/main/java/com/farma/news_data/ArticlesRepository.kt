package com.farma.news_data

import com.farma.database.NewsDatabase
import com.farma.newsapi.NewsApi

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) {

//    suspend fun getAll(): Flow<Article>{
//        api.everything()
//    }
}