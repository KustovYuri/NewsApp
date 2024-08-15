package com.farma.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Response<E>(
    @SerialName("status")
    val status:String,
    @SerialName("totalResult")
    val totalResult:Int,
    @SerialName("articles")
    val articles:List<E>
)