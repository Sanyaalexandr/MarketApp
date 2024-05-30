package com.example.marketapp.data.repository.categories

import android.util.Log
import com.example.marketapp.data.api.MarketApi
import com.example.marketapp.data.model.category.CategoryResponse
import java.util.NoSuchElementException
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val marketApi: MarketApi,
): CategoriesRepository {
    override suspend fun getCategories(): Result<List<CategoryResponse>> =
        try {
            marketApi.getCategories().body()?.let { categories ->
                Result.success(categories)
            } ?: Result.failure(NoSuchElementException("categories not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
}