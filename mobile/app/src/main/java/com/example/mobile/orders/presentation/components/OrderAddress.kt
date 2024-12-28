package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun OrderAddress(
    modifier: Modifier = Modifier,
    address: String,
    instructions: String,
    onAddressChange: (String) -> Unit,
    onInstructionsChange: (String) -> Unit
) {
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Card(
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                value = address,
                onValueChange = {it -> onAddressChange(it)},
                placeholder = {
                    Text(
                        text = stringResource(R.string.delivery_address),
                        color = contentColor
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = contentColor,
                    unfocusedIndicatorColor = contentColor,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = MaterialTheme.shapes.small
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                value = instructions,
                onValueChange = {it -> onInstructionsChange(it)},
                placeholder = {
                    Text(
                        text = "*"+stringResource(R.string.instructions_for_the_courier),
                        color = contentColor
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = contentColor,
                    unfocusedIndicatorColor = contentColor,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = MaterialTheme.shapes.small
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        top = 15.dp,
                        bottom = 10.dp
                    ),
                text = stringResource(R.string.required_fields),
                fontSize = 16.sp,
                color = Color.LightGray
            )
        }
    }
}

@PreviewLightDark
@Composable
fun OrderAddressPreview(){
    MobileTheme {
        OrderAddress(
            address = "Test Address",
            instructions = "Hello World",
            onAddressChange = {},
            onInstructionsChange = {}
        )
    }
}