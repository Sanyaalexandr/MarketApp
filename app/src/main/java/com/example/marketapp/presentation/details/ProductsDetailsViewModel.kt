package com.example.marketapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketapp.data.model.products.ProductResponse
import com.example.marketapp.data.repository.products.ProductsRepository
import com.example.marketapp.presentation.model.ProductItem
import com.example.marketapp.presentation.model.toProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductsDetailsScreenState {
    data object Loading: ProductsDetailsScreenState
    data class Success(val product: ProductItem) : ProductsDetailsScreenState
    data object Error: ProductsDetailsScreenState
}

sealed interface ProductsDetailsScreenEvent {
    data class SelectProduct(val id: Int): ProductsDetailsScreenEvent
    data object ReloadPage: ProductsDetailsScreenEvent
}

@HiltViewModel
class ProductsDetailsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {
    private var selectedProductId: Int? = null

    private val _screenState =
        MutableStateFlow<ProductsDetailsScreenState>(
            ProductsDetailsScreenState.Loading
        )
    val screenState = _screenState.asStateFlow()

    fun onEvent(event: ProductsDetailsScreenEvent) {
        when (event) {
            is ProductsDetailsScreenEvent.SelectProduct -> onLoadProduct(event.id)
            ProductsDetailsScreenEvent.ReloadPage -> onReloadProduct()
        }
    }

    private fun onLoadProduct(id: Int) {
        selectedProductId = id
        viewModelScope.launch(Dispatchers.IO) {
            onLoading()
            productsRepository
                .getProduct(id)
                .onSuccess { productResponse ->
                    onProductLoaded(productResponse)
                }
                .onFailure { onError() }
        }
    }

    private fun onReloadProduct() {
        selectedProductId?.let { id ->
            onLoadProduct(id)
        }
    }

    private fun onProductLoaded(productResponse: ProductResponse) {
        _screenState.update {
            ProductsDetailsScreenState.Success(
                product = productResponse.toProductItem()
            )
        }
    }

    private fun onError() {
        _screenState.update {
            ProductsDetailsScreenState.Error
        }
    }

    private fun onLoading() {
        _screenState.update {
            ProductsDetailsScreenState.Loading
        }
    }
}