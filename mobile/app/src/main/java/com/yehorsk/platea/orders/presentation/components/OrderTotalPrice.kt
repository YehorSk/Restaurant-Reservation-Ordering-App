package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun TotalPrice(
    price: String,
    modifier: Modifier = Modifier
){
    Card {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ){
            Row {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 15.dp,
                            bottom = 10.dp
                        )
                        .weight(1f),
                    text = stringResource(R.string.total),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 15.dp,
                            bottom = 15.dp
                        )
                        .weight(1f),
                    text = "€ $price",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TotalPricePreview(){
    MobileTheme {
        TotalPrice(
            price = "8.20"
        )
    }
}