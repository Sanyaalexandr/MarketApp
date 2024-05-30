package com.example.marketapp.data.repository.categories

import com.example.marketapp.data.model.category.CategoryResponse

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<CategoryResponse>>
}