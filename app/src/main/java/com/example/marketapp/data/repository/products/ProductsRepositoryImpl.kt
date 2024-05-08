package com.example.marketapp.data.repository.products

import android.util.Log
import com.example.marketapp.data.model.products.ProductResponse
import com.example.marketapp.data.api.MarketApi
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val marketApi: MarketApi,
): ProductsRepository {

    override suspend fun getProducts(
        page: Int,
        pageSize: Int,
        category: String?
    ): Result<List<ProductResponse>> =
        try {
            val skip = page * pageSize
            val products = getProductsByCategory(
                skip = skip,
                limit = pageSize,
                category = category
            ).body()?.products ?: emptyList()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getProduct(
        id: Int
    ): Result<ProductResponse> =
        try {
            val productResponse = marketApi.getProduct(
                id = id
            ).body()
            if (productResponse != null) Result.success(productResponse)
            else Result.failure(NoSuchElementException("product with id: $id not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun searchProducts(
        page: Int,
        pageSize: Int,
        searchString: String
    ): Result<List<ProductResponse>> =
        try {
            val skip = page * pageSize
            val productResponse = marketApi.searchProducts(
                q = searchString,
                skip = skip,
                limit = pageSize
            ).body()?.products ?: emptyList()
            Result.success(productResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }

    private suspend fun getProductsByCategory(
        skip: Int,
        limit: Int,
        category: String?,
    ) = if (category == null) {
        marketApi.getProducts(
            skip = skip,
            limit = limit,
        )
    } else {
        marketApi.getProducts(
            skip = skip,
            limit = limit,
            category = category,
        )
    }
}