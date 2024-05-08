package com.example.marketapp.data.model.products

import com.example.marketapp.data.model.products.ProductResponse

data class ProductsListResponse(
    val products: List<ProductResponse>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)