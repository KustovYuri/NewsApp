package com.farma.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.farma.main.State.Error
import com.farma.main.State.Loading
import com.farma.main.State.None
import com.farma.news.uikit.NewsTheme
import com.farma.news_main.R

@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel = viewModel())
}

@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()
    val currentState = state
    if (currentState != None) {
        NewsMainContent(currentState)
    }
}

@Composable
private fun NewsMainContent(currentState: State) {
    Column {
        if (currentState is Error) {
            ErrorMessage(state = currentState)
        }
        if (currentState is Loading) {
            ProgressIndicator(state = currentState)
        }
        if (currentState.articleUIs != null) {
            Articles(articles = currentState.articleUIs)
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun ProgressIndicator(state: Loading) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
private fun ErrorMessage(state: Error) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NewsTheme.colorScheme.error)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error during update", color = NewsTheme.colorScheme.onError)
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
    @PreviewParameter(ArticlesUiPreviewProvider::class, limit = 1) articles: List<ArticleUI>
) {
    LazyColumn {
        items(items = articles) { article ->
            Article(article)
            HorizontalDivider(color = Color.Black)
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun Article(
    @PreviewParameter(ArticleUiPreviewProvider::class, limit = 1) article: ArticleUI
) {
    Row {
        article.imageUrl?.let { imageUrl ->
            var isImageVisible by remember { mutableStateOf(true) }
            if (isImageVisible) {
                AsyncImage(
                    modifier = Modifier.size(150.dp),
                    model = imageUrl,
                    onState = { state ->
                        if (state is AsyncImagePainter.State.Error) {
                            isImageVisible = false
                        }
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.content_description_item_article_image)
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = article.title.toString(),
                style = NewsTheme.typography.headlineMedium,
                maxLines = 1,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.description.toString(),
                style = NewsTheme.typography.headlineSmall,
                maxLines = 3,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}

private class ArticlesUiPreviewProvider : PreviewParameterProvider<List<ArticleUI>> {
    val articlesProvider = ArticleUiPreviewProvider()

    override val values = sequenceOf(
        articlesProvider.values.toList()
    )
}

private class ArticleUiPreviewProvider : PreviewParameterProvider<ArticleUI> {
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
