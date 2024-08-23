package com.farma.news_main

import com.farma.news_data.RequestResult
import com.farma.news_data.models.Article


fun RequestResult<List<ArticleUI>>.toState():State {
    return when(this){
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}

fun Article.toUiArticle():ArticleUI{
    return ArticleUI(
        id = cacheId,
        title = title,
        description = description,
        imageUrl = urlToImage,
        url = url
    )
}