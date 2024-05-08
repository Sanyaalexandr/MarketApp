package com.example.marketapp.data.repository.categories

import com.example.marketapp.data.api.MarketApi
import java.util.NoSuchElementException
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val marketApi: MarketApi,
): CategoriesRepository {
    override suspend fun getCategories(): Result<List<String>> =
        try {
            marketApi.getCategories().body()?.let { categories ->
                Result.success(categories)
            } ?: Result.failure(NoSuchElementException("categories not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
}