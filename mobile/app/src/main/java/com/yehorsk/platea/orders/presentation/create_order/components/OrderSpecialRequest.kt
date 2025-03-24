package com.yehorsk.platea.orders.presentation.create_order.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.yehorsk.platea.R

@Composable
fun OrderSpecialRequest(
    request: String,
    onRequestChange: (String) -> Unit
){

    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = request,
        placeholder = {
            Text(
                text = stringResource(R.string.notes_for_order),
            )
        },
        onValueChange = {
            onRequestChange(it)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}