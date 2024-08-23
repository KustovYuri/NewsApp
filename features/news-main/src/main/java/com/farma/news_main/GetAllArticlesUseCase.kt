package com.farma.news_main

import com.farma.news_data.ArticlesRepository
import com.farma.news_data.RequestResult
import com.farma.news_data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {
    operator fun invoke(): Flow<RequestResult<List<ArticleUI>>>{
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles->
                    articles.map { it.toUiArticle() }
                }
            }
    }
}