package com.farma.news_main

sealed class State{
    data object None: State()
    class Loading(val articleUIs: List<ArticleUI>? = null): State()
    class Error(val articleUIs: List<ArticleUI>? = null): State()
    class Success(val articleUIs: List<ArticleUI>): State()
}