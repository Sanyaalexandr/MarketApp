package com.example.marketapp.presentation.catalog.produtslist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
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
        onProductClick = onProductClick,
        onResetClick = {
            categoriesViewModel.onEvent(CategoriesScreenEvent.CategoryDropped)
        },
        onSelectCategoriesClick = onSelectCategoriesClick,
        onEvent = productsListViewModel::onEvent,
    )
}