package com.farma.news_data

import com.farma.database.NewsDatabase
import com.farma.database.models.ArticleDBO
import com.farma.news_data.models.Article
import com.farma.newsapi.NewsApi
import com.farma.newsapi.models.ArticleDTO
import com.farma.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) {

    suspend fun getAll(): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult.Success<List<ArticleDBO>>> = getAllFromDatabase()

        val remoteArticles = getAllFromServer()

        TODO("WORKING")
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        return flow {
            emit(RequestResult.InProgress())
            emit(api.everything().toRequestResult())
        }
            .onEach { requestResult ->
                if (requestResult is RequestResult.Success) {
                    saveNetResponseToCache(checkNotNull(requestResult.data).articles)
                }
            }
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
    }
}

sealed class RequestResult<E>(internal val data: E? = null) {
    class InProgress<E>(data: E? = null) : RequestResult<E>(data)
    class Success<E>(data: E) : RequestResult<E>(data)
    class Error<E> : RequestResult<E>()
}

internal fun <T : Any> RequestResult<T?>.requireData(): T = checkNotNull(data)

internal fun <I, O> RequestResult<I>.map(mapper: (I?) -> O): RequestResult<O> {
    val outData = mapper(data)
    return when (this) {
        is RequestResult.Error -> RequestResult.Error()
        is RequestResult.InProgress -> RequestResult.InProgress(outData)
        is RequestResult.Success -> RequestResult.InProgress(outData)
    }
}

internal fun <T> Result<T>.toRequestResult(): RequestResult<T>{
    return when{
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Impossible branch")
    }
}