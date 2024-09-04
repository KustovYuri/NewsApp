package com.farma.main

import com.farma.data.ArticlesRepository
import com.farma.data.RequestResult
import com.farma.data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {
    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query)
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toUiArticle() }
                }
            }
    }
}
