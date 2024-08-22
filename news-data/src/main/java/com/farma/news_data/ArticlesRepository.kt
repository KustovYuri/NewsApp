package com.farma.news_data

import com.farma.database.NewsDatabase
import com.farma.database.models.ArticleDBO
import com.farma.news_data.models.Article
import com.farma.newsapi.NewsApi
import com.farma.newsapi.models.ArticleDTO
import com.farma.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val mergeStrategy: MergeStrategy<RequestResult<List<Article>>>,
) {

     fun getAll(): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles = getAllFromDatabase()
            .map { result->
                result.map { articlesDbos->
                    articlesDbos.map { it.toArticle() }
                }
            }

        val remoteArticles = getAllFromServer()
            .map { result->
                result.map { response->
                    response.articles.map { it.toArticle() }
                }
            }

        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result->
                if (result.isSuccess){
                    saveNetResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }.map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(start, apiRequest)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
    }
}