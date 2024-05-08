package com.example.marketapp.presentation.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marketapp.R

@Composable
fun ProductImage(
    data: String,
    placeholder: Painter,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .crossfade(true)
            .build(),
        placeholder = placeholder,
        error = placeholder,
        contentScale = contentScale,
        contentDescription = "Product thumbnail",
        modifier = modifier
    )
}

@Preview
@Composable
private fun ProductImagePreview() {
    ProductImage(
        data = "",
        placeholder = painterResource(id = R.drawable.placeholder),
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
    )
}