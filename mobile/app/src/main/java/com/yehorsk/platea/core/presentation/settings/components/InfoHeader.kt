package com.yehorsk.platea.core.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.ui.theme.MobileThemePreview

@Composable
fun InfoHeader(
    modifier: Modifier = Modifier,
    title: String,
    address: String
){
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp
            ),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 10.dp,
                bottom = 15.dp
            ),
            text = address,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun InfoHeaderPreview(){
    MobileThemePreview {
        InfoHeader(
            title = "Platea",
            address = "Farska 1315/41"
        )
    }
}