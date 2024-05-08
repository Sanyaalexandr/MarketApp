package com.example.marketapp.presentation.catalog.produtslist

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.example.marketapp.presentation.catalog.categories.CategoriesScreenEvent
import com.example.marketapp.presentation.catalog.categories.CategoriesScreenState
import com.example.marketapp.presentation.catalog.categories.CategoriesViewModel

@Composable
fun ProductsListScreen(
    categoriesViewModel: CategoriesViewModel,
    onProductClick: (Int) -> Unit = {},
    onSelectCategoriesClick: () -> Unit = {},
) {
    val productsListViewModel = hiltViewModel<ProductsListViewModel>()
    val screenState = productsListViewModel
        .screenState
        .collectAsState()
        .value

    val scrollState = rememberLazyGridState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = Unit) {
        productsListViewModel.screenEffect.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = Lifecycle.State.STARTED,
        ).collect { effect ->
            when (effect) {
                ProductsListScreenEffect.ResetScrollState -> {
                    scrollState.scrollToItem(0)
                }
            }
        }
    }

    val categoriesScreenState = categoriesViewModel
        .screenState
        .collectAsState()
        .value as? CategoriesScreenState.Success
    val selectedCategory = categoriesScreenState
        ?.categories
        ?.firstOrNull { category -> category.isSelected }

    LaunchedEffect(key1 = selectedCategory) {
        productsListViewModel.onEvent(
            ProductsListScreenEvent.CategorySelected(selectedCategory?.title)
        )
    }

    ProductsListScreenContent(
        state = screenState,
        scrollState = scrollState,
        onProductClick = onProductClick,
        onResetCategory = {
            categoriesViewModel.onEvent(CategoriesScreenEvent.CategoryDropped)
        },
        onSelectCategoriesClick = onSelectCategoriesClick,
        onEvent = productsListViewModel::onEvent,
    )
}