package com.farma.news_main

import com.farma.news_data.ArticlesRepository
import com.farma.news_data.RequestResult
import com.farma.news_data.map
import com.farma.news_data.models.DataArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {
    operator fun invoke(): Flow<RequestResult<List<Article>>>{
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles->
                    articles.map { it.toUiArticle() }
                }
            }
    }
}

private fun DataArticle.toUiArticle():Article{
    TODO("Not yet implemented")
}