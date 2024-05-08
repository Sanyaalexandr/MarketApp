package com.example.marketapp.presentation.catalog.produtslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketapp.SingleEventFlow
import com.example.marketapp.data.model.products.ProductResponse
import com.example.marketapp.data.repository.products.ProductsRepository
import com.example.marketapp.domain.pagination.PaginatorImpl
import com.example.marketapp.presentation.model.ProductItem
import com.example.marketapp.presentation.model.toProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductsListScreenState(
    val isLoading: Boolean = false,
    val products: ImmutableList<ProductItem> = persistentListOf(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0,
    val selectedCategory: String? = null,
    val searchString: String = "",
)

sealed interface ProductsListScreenEvent {
    data object LoadProducts: ProductsListScreenEvent
    data class CategorySelected(val selectedCategory: String?): ProductsListScreenEvent
    data class SearchStringUpdated(val searchString: String): ProductsListScreenEvent
    data object Search: ProductsListScreenEvent
}

sealed interface ProductsListScreenEffect {
    data object ResetScrollState: ProductsListScreenEffect
}

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {
    private val _screenState = MutableStateFlow(ProductsListScreenState())
    val screenState = _screenState.asStateFlow()

    private val _screenEffect = SingleEventFlow<ProductsListScreenEffect>()
    val screenEffect = (_screenEffect as Flow<*>)

    fun onEvent(event: ProductsListScreenEvent) {
        when (event) {
            ProductsListScreenEvent.LoadProducts -> loadNextProducts()
            is ProductsListScreenEvent.CategorySelected -> onCategorySelected(event.selectedCategory)
            is ProductsListScreenEvent.SearchStringUpdated -> onSearchStringUpdated(event.searchString)
            ProductsListScreenEvent.Search -> onSearch()
        }
    }

    private val paginator = PaginatorImpl(
        initialKey = _screenState.value.page,
        onLoadUpdated = ::onLoadUpdated,
        onRequest = { onRequest(it) },
        getNextKey = { _screenState.value.page + 1 },
        onError = ::onError,
        onSuccess = ::onSuccess,
    )

    init {
        loadNextProducts()
    }

    private fun loadNextProducts() =
        viewModelScope.launch {
            paginator.loadNextItems()
        }

    private fun onSearchStringUpdated(searchString: String) {
        _screenState.update { currentState ->
            currentState.copy(
                searchString = searchString
            )
        }
        if (searchString.isBlank()) onSearch()
    }

    private fun onSearch() {
        _screenState.update { currentState ->
            currentState.copy(
                page = 0,
                products = persistentListOf(),
                selectedCategory = null,
            )
        }
        paginator.reset()
        loadNextProducts()
    }

    private fun onCategorySelected(category: String?) {
        if (_screenState.value.selectedCategory != category ||
            _screenState.value.searchString.isNotBlank() && category != null) {
            _screenState.update { currentState ->
                currentState.copy(
                    page = 0,
                    products = persistentListOf(),
                    selectedCategory = category,
                    searchString = "",
                )
            }
            paginator.reset()
            loadNextProducts()
        }
    }

    private fun onLoadUpdated(isLoading: Boolean) {
        _screenState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }

    private suspend fun onRequest(
        nextPage: Int
    ): Result<List<ProductResponse>> =
        if (_screenState.value.searchString.isBlank()) {
            productsRepository.getProducts(
                page = nextPage,
                pageSize = PaginatorImpl.DEFAULT_PAGE_SIZE,
                category = _screenState.value.selectedCategory
            )
        } else {
            productsRepository.searchProducts(
                page = nextPage,
                pageSize = PaginatorImpl.DEFAULT_PAGE_SIZE,
                searchString = _screenState.value.searchString
            )
        }

    private fun onError(exception: Throwable?) {
        _screenState.update { currentScreen ->
            currentScreen.copy(
                error = exception?.localizedMessage,
                endReached = true,
            )
        }
    }

    private fun onSuccess(
        items: List<ProductResponse>,
        newKey: Int
    ) {
        _screenState.update { currentState ->
            currentState.copy(
                products = (
                        currentState.products +
                                items.map { productResponse ->
                                    productResponse.toProductItem()
                                }
                        ).toImmutableList(),
                error = null,
                page = newKey,
                endReached = items.isEmpty(),
            )
        }
        if (_screenState.value.page == 1) {
            _screenEffect.emit(ProductsListScreenEffect.ResetScrollState)
        }
    }
}