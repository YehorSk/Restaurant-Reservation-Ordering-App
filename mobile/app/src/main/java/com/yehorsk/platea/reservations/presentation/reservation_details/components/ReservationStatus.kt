package com.yehorsk.platea.reservations.presentation.reservation_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.Utility.statusToString
import com.yehorsk.platea.core.utils.formatOrderDateTime
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun ReservationStatus(
    modifier: Modifier = Modifier,
    status: String,
    date: String,
    code: String
){
    val context = LocalContext.current
    Card(
    ) {
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
                text = stringResource(R.string.reservation_code, code),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp
                ),
                text = stringResource(R.string.reservation_order_status, statusToString(status, context)),
                style = MaterialTheme.typography.bodyLarge ,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp,
                    bottom = 15.dp
                ),
                text = stringResource(R.string.reservation_order_date, formatOrderDateTime(date)),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
fun OrderStatusPreview(){
    MobileTheme {
        ReservationStatus(
            status = "Pending",
            date = "05 May, 2024 10:40",
            code = "#KE1NB4"
        )
    }
}