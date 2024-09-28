package com.farma.data

import com.farma.data.RequestResult.Error
import com.farma.data.RequestResult.InProgress
import com.farma.data.RequestResult.Success

public interface MergeStrategy<E> {
    public fun merge(right: E, left: E): E
}

/**
 * Дефолтная стратегия мерджа данных из разных источников
 */
internal class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {

    @Suppress("CyclomaticComplexMethod")
    override fun merge(right: RequestResult<T>, left: RequestResult<T>): RequestResult<T> {
        return when {
            right is InProgress && left is InProgress -> merge(right, left)
            right is Success && left is InProgress -> merge(right, left)
            right is InProgress && left is Success -> merge(right, left)
            right is Success && left is Success -> merge(right, left)
            right is Success && left is Error -> merge(right, left)
            right is InProgress && left is Error -> merge(right, left)
            right is Error && left is InProgress -> merge(right, left)
            right is Error && left is Success -> merge(right, left)

            else -> error("Unimplemented branch right=$right & left=$left")
        }
    }

    private fun merge(
        cache: InProgress<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return when {
            server.data != null -> InProgress(server.data)
            else -> InProgress(cache.data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: Success<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return InProgress(cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: InProgress<T>,
        server: Success<T>
    ): RequestResult<T> {
        return InProgress(server.data)
    }

    private fun merge(
        cache: Success<T>,
        server: Error<T>
    ): RequestResult<T> {
        return Error(data = cache.data, error = server.error)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: Success<T>,
        server: Success<T>
    ): RequestResult<T> {
        return Success(data = server.data)
    }

    private fun merge(
        cache: InProgress<T>,
        server: Error<T>
    ): RequestResult<T> {
        return InProgress(data = server.data ?: cache.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: Error<T>,
        server: InProgress<T>
    ): RequestResult<T> {
        return server
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        cache: Error<T>,
        server: Success<T>
    ): RequestResult<T> {
        return server
    }
}
