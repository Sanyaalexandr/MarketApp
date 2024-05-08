package com.example.marketapp.presentation.catalog.categories

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.marketapp.R
import com.example.marketapp.presentation.components.ErrorRetryMessage
import com.example.marketapp.presentation.model.Category
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private typealias CategoriesScreenEvents = (CategoriesScreenEvent) -> Unit

@Composable
fun CategoriesScreenContent(
    state: CategoriesScreenState,
    onEvent: CategoriesScreenEvents,
    onApplyClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(state) {
            CategoriesScreenState.Loading -> LoadingContent()
            CategoriesScreenState.Error -> ErrorContent(
                onReloadClick = { onEvent(CategoriesScreenEvent.ReloadPage) }
            )
            is CategoriesScreenState.Success -> SuccessContent(
                categories = state.categories,
                onEvent = onEvent,
                onApplyClick = onApplyClick,
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
        )
    }
}

@Composable
private fun ErrorContent(
    onReloadClick: () -> Unit,
) {
    ErrorRetryMessage(
        message = stringResource(id = R.string.error_text_wrapped),
        buttonTextStyle = MaterialTheme.typography.titleLarge,
        messageTextStyle = MaterialTheme.typography.headlineMedium,
        onRetryClick = onReloadClick,
        spaceBetween = 16.dp,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
private fun SuccessContent(
    categories: ImmutableList<Category>,
    onEvent: CategoriesScreenEvents = {},
    onApplyClick: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val firstVisibleItemIndex = remember {
        derivedStateOf { scrollState.firstVisibleItemIndex }
    }
    val firstVisibleItemScrollOffset = remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset }
    }
    val topBarElevation by animateDpAsState(
        targetValue = if (firstVisibleItemIndex.value > 0 ||
            firstVisibleItemScrollOffset.value > 0) 20.dp else 0.dp, label = ""
    )
    Column {
        TopBar(
            topBarElevation = topBarElevation,
            onApplyClick = onApplyClick,
            onResetClick = { onEvent(CategoriesScreenEvent.CategoryDropped) }
        )
        Categories(
            scrollState = scrollState,
            categories = categories,
            onSelect = { category, i ->
                val event = if (category.isSelected) {
                    CategoriesScreenEvent.CategoryDropped
                } else {
                    CategoriesScreenEvent.CategorySelected(i)
                }
                onEvent(event)
            }
        )
    }
}

@Composable
private fun Categories(
    scrollState: LazyListState,
    categories: ImmutableList<Category>,
    onSelect: (Category, Int) -> Unit,
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(categories) { i, category ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(category, i) }
            ) {
                Checkbox(
                    checked = category.isSelected,
                    onCheckedChange = {
                        onSelect(category, i)
                    }
                )
                Text(text = category.title)
            }
        }
        item { 
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TopBar(
    topBarElevation: Dp,
    onApplyClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    Surface(
        shadowElevation = topBarElevation,
        shape = RoundedCornerShape(
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = onApplyClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
            ) {
                Text(text = "Применить")
            }
            Button(
                onClick = onResetClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
            ) {
                Text(text = "Сбросить")
            }
        }
    }
}

@Preview
@Composable
private fun SuccessContentPreview() {
    SuccessContent(
        categories = persistentListOf(
            Category("category1", true),
            Category("category2", false),
        )
    )
}