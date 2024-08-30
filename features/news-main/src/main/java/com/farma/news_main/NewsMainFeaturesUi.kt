package com.farma.news_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farma.news_main.State.*


@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()

    when(val currentState = state){
        is Success -> Articles(currentState.articleUIs)
        is Error -> ArticlesWithError(currentState.articleUIs)
        is Loading -> ArticlesDuringUpdate(currentState.articleUIs)
        None -> NewsEmpty()
    }
}

@Composable
internal fun ArticlesWithError(articles: List<ArticleUI>?) {
    Column {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ){
            Text(text = "Error during update", color = MaterialTheme.colorScheme.onError)
        }
        if (articles != null){
            Articles(articles = articles)
        }
    }
}

@Composable
internal fun ArticlesDuringUpdate(
    @PreviewParameter(ArticlesUiPreviewProvider::class, limit = 1)
    articles: List<ArticleUI>?
) {
    Column {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
        if (articles != null){
            Articles(articles = articles)
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


@Composable
private fun Articles(
    @PreviewParameter(ArticlesUiPreviewProvider::class, limit = 1)
    articles: List<ArticleUI>
) {
    LazyColumn {
        items(items = articles, key = {it.id}){ article->
            Article(article)
            HorizontalDivider(color = Color.Black)
        }
    }
}

@Preview
@Composable
private fun Article(
    @PreviewParameter(ArticleUiPreviewProvider::class, limit = 1)
    article: ArticleUI
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = article.title.toString(), style = MaterialTheme.typography.headlineMedium, maxLines = 1)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = article.description.toString(), style = MaterialTheme.typography.headlineSmall, maxLines = 3)
    }
}

private class ArticlesUiPreviewProvider : PreviewParameterProvider<List<ArticleUI>>{
    val articlesProvider = ArticleUiPreviewProvider()

    override val values = sequenceOf(
        articlesProvider.values
            .toList()
    )
}

private class ArticleUiPreviewProvider : PreviewParameterProvider<ArticleUI>{
    override val values = sequenceOf(
        ArticleUI(
            id = 1,
            title = "Android Studio Iguana is Stable!",
            description = "New stable version on Android IDE has been realised",
            imageUrl = null,
            url = null
        ),
        ArticleUI(
            id = 2,
            title = "Gemini 1.5 Release",
            description = "Updated version of Google AI is available",
            imageUrl = null,
            url = null
        ),
        ArticleUI(
            id = 3,
            title = "Shape animations (10 min)",
            description = "How to use shape animation in Compose",
            imageUrl = null,
            url = null
        ),

    )
}
