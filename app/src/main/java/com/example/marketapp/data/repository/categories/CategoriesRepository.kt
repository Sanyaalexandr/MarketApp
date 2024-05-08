package com.example.marketapp.data.repository.categories

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<String>>
}