package com.example.marketapp.presentation.catalog.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketapp.data.repository.categories.CategoriesRepository
import com.example.marketapp.presentation.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CategoriesScreenState {
    data object Loading: CategoriesScreenState
    data object Error: CategoriesScreenState
    data class Success(
        val categories: ImmutableList<Category> = persistentListOf(),
    ): CategoriesScreenState
}

sealed interface CategoriesScreenEvent {
    data class CategorySelected(val categoryIndex: Int): CategoriesScreenEvent
    data object CategoryDropped: CategoriesScreenEvent

    data object ReloadPage: CategoriesScreenEvent
}

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
): ViewModel() {
    private val _screenState = MutableStateFlow<CategoriesScreenState>(
        CategoriesScreenState.Loading
    )
    val screenState = _screenState.asStateFlow()

    fun onEvent(event: CategoriesScreenEvent) {
        when(event) {
            is CategoriesScreenEvent.CategorySelected -> onCategorySelected(
                categoryIndex = event.categoryIndex
            )
            CategoriesScreenEvent.CategoryDropped -> onCategorySelected(
                categoryIndex = null
            )
            CategoriesScreenEvent.ReloadPage -> loadCategories()
        }
    }

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.update { currentState ->
                val categories = categoriesRepository.getCategories()
                    .getOrElse { return@update CategoriesScreenState.Error }
                    .map {
                        category -> Category(
                            title = category,
                            isSelected = false,
                        )
                    }
                    .toImmutableList()
                if (currentState is CategoriesScreenState.Success) {
                    currentState.copy(categories = categories)
                } else {
                    CategoriesScreenState.Success(categories = categories)
                }
            }
        }
    }

    private fun onCategorySelected(categoryIndex: Int?) {
        _screenState.update { currentState ->
            if (currentState is CategoriesScreenState.Success) {
                val newCategories = currentState.updateSelectedCategory(categoryIndex)
                currentState.copy(
                    categories = newCategories.toImmutableList()
                )
            } else {
                CategoriesScreenState.Error
            }
        }
    }

    private fun CategoriesScreenState.Success.updateSelectedCategory(
        index: Int?
    ): ImmutableList<Category> = this
        .categories
        .mapIndexed { i, category ->
            category.copy(
                isSelected = i == index,
            )
        }
        .toImmutableList()
}
