package com.farma.news_main

sealed class State(val articleUIs: List<ArticleUI>?){
    data object None: State(articleUIs = null)
    class Loading(articleUIs: List<ArticleUI>? = null): State(articleUIs)
    class Error(articleUIs: List<ArticleUI>? = null): State(articleUIs)
    class Success(articleUIs: List<ArticleUI>): State(articleUIs)
}