package com.example.marketapp.data.repository.products

import com.example.marketapp.data.model.product.ProductResponse

interface ProductsRepository {

    suspend fun getProducts(
        page: Int,
        pageSize: Int,
        category: String?,
    ): Result<List<ProductResponse>>

    suspend fun getProduct(
        id: Int,
    ): Result<ProductResponse>

    suspend fun searchProducts(
        page: Int,
        pageSize: Int,
        searchString: String,
    ): Result<List<ProductResponse>>
}