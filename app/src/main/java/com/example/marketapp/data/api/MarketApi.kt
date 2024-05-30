package com.example.marketapp.data.api

import com.example.marketapp.data.model.category.CategoryResponse
import com.example.marketapp.data.model.product.ProductResponse
import com.example.marketapp.data.model.product.ProductsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarketApi {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Response<ProductsListResponse>

    @GET("products/category/{category}")
    suspend fun getProducts(
        @Path("category") category: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Response<ProductsListResponse>

    @GET("product/{id}")
    suspend fun getProduct(
        @Path("id") id: Int,
    ): Response<ProductResponse>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") q: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Response<ProductsListResponse>
}