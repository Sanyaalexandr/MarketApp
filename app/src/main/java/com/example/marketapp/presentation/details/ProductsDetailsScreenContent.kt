package com.example.marketapp.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marketapp.R
import com.example.marketapp.presentation.components.ErrorRetryMessage
import com.example.marketapp.presentation.components.ProductDetailsItem
import com.example.marketapp.presentation.model.ProductItem

private typealias ProductsDetailsScreenEvents = (ProductsDetailsScreenEvent) -> Unit

@Composable
fun ProductsDetailsScreenContent(
    state: ProductsDetailsScreenState,
    onEvent: ProductsDetailsScreenEvents,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(state) {
            ProductsDetailsScreenState.Loading -> LoadingContent()
            ProductsDetailsScreenState.Error -> ErrorContent(
                onEvent = onEvent,
            )
            is ProductsDetailsScreenState.Success -> SuccessContent(
                product = state.product,
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
    onEvent: ProductsDetailsScreenEvents,
) {
    ErrorRetryMessage(
        message = stringResource(id = R.string.error_text_wrapped),
        buttonTextStyle = MaterialTheme.typography.titleLarge,
        messageTextStyle = MaterialTheme.typography.headlineMedium,
        onRetryClick = {
            onEvent(ProductsDetailsScreenEvent.ReloadPage)
        },
        spaceBetween = 16.dp,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuccessContent(
    product: ProductItem,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        ProductDetailsItem(product = product)
    }
}

@Preview
@Composable
private fun ProductsDetailsScreenContentErrorPreview() {
    ProductsDetailsScreenContent(
        state = ProductsDetailsScreenState.Error,
        onEvent = {},
    )
}

@Preview
@Composable
private fun ProductsDetailsScreenContentSuccessPreview() {
    ProductsDetailsScreenContent(
        state = ProductsDetailsScreenState.Success(
            product = ProductItem(
                id = 1,
                price = 1000,
                title = "title",
                description = "description description description description description ",
                thumbnail = "",
                images = emptyList(),
                rating = 4.56f,
            ),
        ),
        onEvent = {},
    )
}