package com.example.marketapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marketapp.R
import com.example.marketapp.presentation.model.ProductItem

private val itemRoundedCornerSize = 12.dp
private const val IMAGE_RATIO = 0.8f

@Composable
fun ProductListItem(
    product: ProductItem,
    onItemClick: (Int) -> Unit = {},
) {
    val placeholder = painterResource(id = R.drawable.placeholder)
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(itemRoundedCornerSize),
    ) {
        Column(
            modifier = Modifier
                .clickable { onItemClick.invoke(product.id) }
                .padding(2.dp)
        ) {
            ProductImage(
                data = product.thumbnail,
                placeholder = placeholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(IMAGE_RATIO)
                    .clip(RoundedCornerShape(itemRoundedCornerSize)),
            )
            Column(
                modifier = Modifier
                    .padding(
                        start = 4.dp,
                        end = 4.dp,
                        top = 2.dp,
                        bottom = 2.dp
                    )
            ) {
                Text(
                    text = "${product.price} \$",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.title,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.description,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.6f
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductListItemPreview() {
    ProductListItem(
        product = ProductItem(
            id = 1,
            price = 1000,
            title = "title",
            description = "description description description description description ",
            thumbnail = "",
            images = emptyList(),
            rating = 4.56f,
        ),
    )
}
