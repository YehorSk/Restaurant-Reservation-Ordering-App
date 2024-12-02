package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.mobile.R

@Composable
fun OrderSpecialRequest(
    request: String,
    onRequestChange: (String) -> Unit
){
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        value = request,
        placeholder = {
            Text(
                text = stringResource(R.string.notes_for_order)
            )
        },
        onValueChange = {
            onRequestChange(it)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}