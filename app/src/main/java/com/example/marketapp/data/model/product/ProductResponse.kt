package com.example.marketapp.data.model.product

data class ProductResponse(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Float,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val thumbnail: String,
    val images: List<String>
)
