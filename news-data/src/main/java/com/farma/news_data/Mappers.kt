package com.farma.news_data

import com.farma.database.models.ArticleDBO
import com.farma.database.models.SourceDBO
import com.farma.news_data.models.Article
import com.farma.news_data.models.Source
import com.farma.newsapi.models.ArticleDTO
import com.farma.newsapi.models.SourceDTO

internal fun ArticleDBO.toArticle(): Article {
    return Article(
        cacheId = id,
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

internal fun SourceDBO.toSource(): Source {
    return Source(id = id, name = name)
}

internal fun SourceDTO.toSource(): Source {
    return Source(id = id ?: "name", name = name ?: "name")
}

internal fun SourceDTO.toSourceDbo(): SourceDBO {
    return SourceDBO(id = id ?: "name", name = name ?: "name")
}

internal fun ArticleDTO.toArticle(): Article {
    return Article(
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

internal fun ArticleDTO.toArticleDbo(): ArticleDBO {
    return ArticleDBO(
        source = source?.toSourceDbo(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

