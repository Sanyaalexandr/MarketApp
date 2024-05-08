package com.example.marketapp.presentation.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProductsDetailsScreen(
    productId: Int,
) {
    val viewModel = hiltViewModel<ProductsDetailsViewModel>()
    LaunchedEffect(key1 = productId) {
        viewModel.onEvent(ProductsDetailsScreenEvent.SelectProduct(productId))
    }
    val state = viewModel.screenState.collectAsState()
    ProductsDetailsScreenContent(
        state = state.value,
        onEvent = viewModel::onEvent
    )
}