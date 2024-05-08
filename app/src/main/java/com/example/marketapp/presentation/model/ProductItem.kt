package com.example.marketapp.presentation.model

import androidx.compose.runtime.Immutable
import com.example.marketapp.data.model.products.ProductResponse

@Immutable
data class ProductItem(
    val id: Int,
    val price: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val images: List<String>,
    val rating: Float,
)

fun ProductResponse.toProductItem() =
    ProductItem(
        id = id,
        price = price,
        title = title,
        description = description,
        thumbnail = thumbnail,
        images = images,
        rating = rating,
    )
