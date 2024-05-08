package com.example.marketapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.marketapp.R

@Composable
fun ErrorRetryMessage(
    message: String,
    buttonTextStyle: TextStyle,
    messageTextStyle: TextStyle,
    spaceBetween: Dp,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = messageTextStyle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(spaceBetween))
        Button(
            onClick = { onRetryClick.invoke() },
        ) {
            Text(
                text = stringResource(id = R.string.update),
                style = buttonTextStyle
            )
        }
    }
}