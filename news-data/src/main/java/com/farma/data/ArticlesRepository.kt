package com.farma.data

import com.farma.common.Logger
import com.farma.data.models.Article
import com.farma.database.NewsDatabase
import com.farma.database.models.ArticleDBO
import com.farma.newsapi.NewsApi
import com.farma.newsapi.models.ArticleDTO
import com.farma.newsapi.models.ResponseDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val logger: Logger
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(
        query: String,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles = getAllFromDatabase()

        val remoteArticles = getAllFromServer(query)

        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    database.articlesDao.observeAll()
                        .map { dbos -> dbos.map { it.toArticle() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    private fun getAllFromServer(query: String): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything(query = query)) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .onEach { result ->
                if (result.isFailure) {
                    logger.e(
                        LOG_TAG,
                        "Error getting from server. Cause = ${result.exceptionOrNull()}"
                    )
                }
            }.map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge(start, apiRequest)
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<Article>>> {
        val dbRequest = database.articlesDao::getAll.asFlow()
            .map { RequestResult.Success(it) }
            .catch {
                RequestResult.Error<List<ArticleDBO>>(error = it)
                logger.e(LOG_TAG, "Error getting from database. Cause = $it")
            }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest)
            .map { result ->
                result.map { articlesDbos ->
                    articlesDbos.map { it.toArticle() }
                }
            }
    }

    private companion object {

        const val LOG_TAG = "ArticlesRepository"
    }
}
