package com.farma.newsapi

import androidx.annotation.IntRange
import com.farma.newsapi.models.Article
import com.farma.newsapi.models.Language
import com.farma.newsapi.models.Response
import com.farma.newsapi.models.SortBy
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 * [API Documentation](https://newsapi.org/docs/get-started)
 */
interface NewsApi {
    /**
     * API details [here](https://newsapi.org/docs/endpoints/everything)
     */
    @GET("/everything")
    suspend fun everything(
        @Query("q") query:String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize:Int = 100,
        @Query("page") @IntRange(from = 1)page:Int = 1,
    ): Response<Article>
}

fun NewsApi(
    baseUrl:String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json,
): NewsApi {
    return retrofit(baseUrl, okHttpClient, json).create()
}

private fun retrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient?,
    json: Json,
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory(MediaType.get("application/json"))
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .run { if (okHttpClient != null) client(okHttpClient) else this }
        .build()
}

