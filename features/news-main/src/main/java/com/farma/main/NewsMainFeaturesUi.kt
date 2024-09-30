package com.farma.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farma.news.uikit.NewsTheme

@Composable
fun NewsMainScreen(modifier: Modifier = Modifier) {
    NewsMainScreen(modifier, viewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(modifier: Modifier = Modifier, viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    if (currentState != State.None) {
        NewsMainContent(modifier, currentState)
    }
}

@Composable
private fun NewsMainContent(modifier: Modifier = Modifier, currentState: State) {
    Column(modifier) {
        when (currentState) {
            is State.None -> Unit
            is State.Success -> ArticlesList(articlesState = currentState)
            is State.Loading -> ProgressIndicator(state = currentState)
            is State.Error -> ErrorMessage(state = currentState)
        }
    }
}

@Composable
private fun ProgressIndicator(state: State.Loading) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    if (state.articleUIs != null) {
        ArticlesList(articlesState = state.articleUIs)
    }
}

@Composable
private fun ErrorMessage(state: State.Error) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NewsTheme.colorScheme.error)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error during update", color = NewsTheme.colorScheme.onError)
        }
        if (state.articleUIs != null) {
            ArticlesList(articlesState = state.articleUIs)
        }
    }
}

@Composable
internal fun NewsEmpty() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No news")
    }
}
