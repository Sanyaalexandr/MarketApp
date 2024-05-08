package com.example.marketapp.presentation.catalog.produtslist

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.marketapp.R
import com.example.marketapp.presentation.components.ErrorRetryMessage
import com.example.marketapp.presentation.components.ProductListItem
import com.example.marketapp.presentation.model.ProductItem
import kotlinx.collections.immutable.persistentListOf

private typealias ProductsListScreenEvents = (ProductsListScreenEvent) -> Unit

@Composable
fun ProductsListScreenContent(
    state: ProductsListScreenState,
    scrollState: LazyGridState = rememberLazyGridState(),
    onResetCategory: () -> Unit = {},
    onSelectCategoriesClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {},
    onEvent: ProductsListScreenEvents = {},
) {
    val firstVisibleItemIndex by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex }
    }
    val firstVisibleItemScrollOffset by remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset }
    }
    val isContentScrolled = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0
    val categorySelectorElevation by animateDpAsState(
        targetValue = if (isContentScrolled) 5.dp else 0.dp, label = ""
    )
    Scaffold(
        topBar = {
            TopBar(
                searchString = state.searchString,
                elevation = categorySelectorElevation,
                selectedCategory = state.selectedCategory,
                onResetCategory = onResetCategory,
                onSelectCategoriesClick = onSelectCategoriesClick,
                onSearchClick = {
                    onResetCategory.invoke()
                    onEvent(ProductsListScreenEvent.Search)
                },
                onClearClick = {
                    onEvent(ProductsListScreenEvent.SearchStringUpdated(""))
                },
                onSearchStringUpdated = { searchString ->
                    onEvent(ProductsListScreenEvent.SearchStringUpdated(searchString))
                }
            )
        }
    ) { innerPaddings ->
        Box(
            modifier = Modifier.padding(innerPaddings).fillMaxWidth()
        ) {
            if (state.error != null && state.products.isEmpty() && !state.isLoading) {
                FullScreenErrorRetryMessage(
                    onRetryClick = { onEvent(ProductsListScreenEvent.LoadProducts) }
                )
            } else if (state.isLoading && state.products.isEmpty()) {
                ProgressIndicator()
            } else {
                ProductsList(
                    state = state,
                    scrollState = scrollState,
                    isFirstItemVisible = firstVisibleItemIndex == 0,
                    onLoadProducts = {
                        onEvent(ProductsListScreenEvent.LoadProducts)
                    },
                    onItemClick = onProductClick,
                    onRetryClick = {
                        onEvent(ProductsListScreenEvent.LoadProducts)
                    }
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    searchString: String,
    elevation: Dp,
    selectedCategory: String?,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit,
    onSearchStringUpdated: (String) -> Unit,
    onResetCategory: () -> Unit,
    onSelectCategoriesClick: () -> Unit,
) {
    Surface(
        shadowElevation = elevation,
        shape = RoundedCornerShape(
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        ),
    ) {
        Column {
            SearchBar(
                searchString = searchString,
                onSearchClick = onSearchClick,
                onSearchStringUpdated = onSearchStringUpdated,
                onClearClick = onClearClick,
            )
            CategorySelector(
                selectedCategory = selectedCategory,
                onResetCategory = onResetCategory,
                onSelectCategoriesClick = onSelectCategoriesClick,
            )
        }
    }
}

@Composable
private fun SearchBar(
    searchString: String,
    onSearchStringUpdated: (String) -> Unit,
    onClearClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            value = searchString,
            onValueChange = onSearchStringUpdated,
            leadingIcon = {
                IconButton(onClick = onSearchClick) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "search icon")
                }
            },
            trailingIcon = {
                IconButton(onClick = onClearClick) {
                    Icon(imageVector = Icons.Rounded.Clear, contentDescription = "clear icon")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                onSearchClick.invoke()
            }),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun CategorySelector(
    selectedCategory: String?,
    onResetCategory: () -> Unit,
    onSelectCategoriesClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = onResetCategory,
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_filter_alt_off_24),
                    contentDescription = "filter icon"
                )
                Text(text = stringResource(id = R.string.reset))
            }
            TextButton(
                onClick = onSelectCategoriesClick,
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_filter_alt_24),
                    contentDescription = "filter icon"
                )
                Text(text = stringResource(id = R.string.select_category))
            }
        }
        selectedCategory?.let { category ->
            val selectedCategoryTitle = stringResource(id = R.string.selected_category)
            Text(
                text = "$selectedCategoryTitle: $category",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun FullScreenErrorRetryMessage(
    onRetryClick: () -> Unit,
) {
    ErrorRetryMessage(
        message = stringResource(id = R.string.error_text_wrapped),
        buttonTextStyle = MaterialTheme.typography.titleLarge,
        messageTextStyle = MaterialTheme.typography.headlineMedium,
        onRetryClick = { onRetryClick.invoke() },
        spaceBetween = 16.dp,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
private fun EndOfListErrorRetryMessage(
    onRetryClick: () -> Unit,
) {
    ErrorRetryMessage(
        message = stringResource(id = R.string.error_text),
        buttonTextStyle = MaterialTheme.typography.bodyLarge,
        messageTextStyle = MaterialTheme.typography.titleSmall,
        onRetryClick = { onRetryClick.invoke() },
        spaceBetween = 8.dp,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductsList(
    state: ProductsListScreenState,
    scrollState: LazyGridState,
    isFirstItemVisible: Boolean,
    onLoadProducts: () -> Unit,
    onItemClick: (Int) -> Unit,
    onRetryClick: () -> Unit
) {
    /*
     Оверскрол эффект в компоузе работает как-то криво. При быстром скролле при обновлении списка,
     список дальше не скролится пока не закончится эффект.
     Если быстро скролить, то можно не давать этому эффекту закончиться, соответственно список не будет скролится
     Эта штука лечит этот баг
     */
    val overscrollConfiguration = if (state.endReached || isFirstItemVisible) {
        OverscrollConfiguration()
    } else {
        null
    }
    CompositionLocalProvider(
        value = LocalOverscrollConfiguration provides overscrollConfiguration
    ) {
        LazyVerticalGrid(
            state = scrollState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(
                items = state.products,
                key = { _, product -> product.id }
            ) { i, product ->
                if (i >= state.products.size - 1 && !state.endReached && !state.isLoading) {
                    onLoadProducts.invoke()
                }
                ProductListItem(
                    product = product,
                    onItemClick = onItemClick,
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                if (state.isLoading) {
                    ProgressIndicator()
                } else if (state.error != null) {
                    EndOfListErrorRetryMessage(
                        onRetryClick = onRetryClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogScreenContentPreview() {
    ProductsListScreenContent(
        state = ProductsListScreenState(
            products = persistentListOf(
                ProductItem(
                    id = 1,
                    price = 1000,
                    title = "title",
                    description = "description description description description description ",
                    thumbnail = "",
                    images = emptyList(),
                    rating = 4.56f,
                ),
                ProductItem(
                    id = 1,
                    price = 1000,
                    title = "title",
                    description = "description description description description description ",
                    thumbnail = "",
                    images = emptyList(),
                    rating = 4.56f,
                ),
                ProductItem(
                    id = 1,
                    price = 1000,
                    title = "title",
                    description = "description description description description description ",
                    thumbnail = "",
                    images = emptyList(),
                    rating = 4.56f,
                ),
                ProductItem(
                    id = 1,
                    price = 1000,
                    title = "title",
                    description = "description description description description description ",
                    thumbnail = "",
                    images = emptyList(),
                    rating = 4.56f,
                ),
                ProductItem(
                    id = 1,
                    price = 1000,
                    title = "title",
                    description = "description description description description description ",
                    thumbnail = "",
                    images = emptyList(),
                    rating = 4.56f,
                ),
                ProductItem(
                    id = 1,
                    price = 1000,
                    title = "title",
                    description = "description description description description description ",
                    thumbnail = "",
                    images = emptyList(),
                    rating = 4.56f,
                ),
            )
        )
    )
}