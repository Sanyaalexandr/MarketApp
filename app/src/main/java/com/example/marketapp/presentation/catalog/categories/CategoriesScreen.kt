package com.example.marketapp.presentation.catalog.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun CategoriesScreen(
    onApplyClick: () -> Unit,
    categoriesViewModel: CategoriesViewModel
) {
    val screenState = categoriesViewModel.screenState.collectAsState().value
    CategoriesScreenContent(
        state = screenState,
        onEvent = categoriesViewModel::onEvent,
        onApplyClick = onApplyClick,
    )
}