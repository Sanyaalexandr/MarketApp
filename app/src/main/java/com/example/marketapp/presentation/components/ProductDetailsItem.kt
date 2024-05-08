package com.example.marketapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marketapp.R
import com.example.marketapp.design.theme.rating
import com.example.marketapp.presentation.model.ProductItem

private const val IMAGE_RATIO = 0.9f

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailsItem(
    product: ProductItem,
) {
    val placeholder = painterResource(id = R.drawable.placeholder)
    val pagerState = rememberPagerState {
        product.images.size
    }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                )
            ),
    ) { pageIndex ->
        ProductImage(
            data = product.images[pageIndex],
            placeholder = placeholder,
            modifier = Modifier
                .aspectRatio(IMAGE_RATIO),
        )
    }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "${product.price} $",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Row {
            Text(
                text = product.rating.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = rating,
            )
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "rating icon",
                tint = rating
            )
        }
        Divider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = product.description,
            style = MaterialTheme.typography.titleLarge
        )
    }
}