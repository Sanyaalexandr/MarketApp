package com.example.marketapp.data.model.product

data class ProductsListResponse(
    val products: List<ProductResponse>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)