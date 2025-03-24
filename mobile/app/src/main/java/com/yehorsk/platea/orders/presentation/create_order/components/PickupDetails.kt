package com.yehorsk.platea.orders.presentation.create_order.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.yehorsk.platea.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickupDetails(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 15.dp,
                    bottom = 10.dp
                ),
            text = stringResource(R.string.platea_restaurant),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            )
        Text(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 10.dp
                ),
            text = "Farska 1315/41, Nitra",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}