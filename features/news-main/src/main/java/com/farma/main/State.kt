package com.farma.main

import androidx.compose.runtime.Immutable

@Immutable
sealed class State(open val articleUIs: List<ArticleUI>?) {
    data object None : State(articleUIs = null)

    class Loading(articleUIs: List<ArticleUI>? = null) : State(articleUIs)

    class Error(articleUIs: List<ArticleUI>? = null) : State(articleUIs)

    class Success(override val articleUIs: List<ArticleUI>) : State(articleUIs)
}
