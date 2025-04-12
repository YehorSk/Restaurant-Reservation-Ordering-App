package com.yehorsk.platea.orders.presentation.create_order.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

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
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@PreviewLightDark
@Composable
fun OrderSpecialRequestPreview(){
    MobileTheme {
        OrderSpecialRequest(
            request = "",
            onRequestChange = {}
        )
    }
}