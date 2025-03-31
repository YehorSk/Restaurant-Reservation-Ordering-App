package com.yehorsk.platea.reservations.presentation.reservation_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.formatDateTime
import com.yehorsk.platea.core.utils.formatTime
import com.yehorsk.platea.ui.theme.MobileTheme
import com.yehorsk.platea.ui.theme.MobileThemePreview

@Composable
fun ReservationDetails(
    modifier: Modifier = Modifier,
    orderCode: String? = null,
    time: String,
    table: String,
    date: String,
    request: String? = null,
    phone: String,
    partySize: String
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
            text = stringResource(R.string.table, table),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp
            ),
            text = stringResource(R.string.date_reservation, formatDateTime(date)),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
            ),
            text = stringResource(R.string.time_reservation, formatTime(time)),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
                bottom = 10.dp
            ),
            text = stringResource(R.string.party_size) + ": $partySize",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        HorizontalDivider()
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
            ),
            text = stringResource(R.string.special_request, request?: ""),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        if(orderCode != null){
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp,
                ),
                text = stringResource(R.string.order_code, orderCode),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
                bottom = 20.dp
            ),
            text = stringResource(R.string.phone_number, phone),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun ReservationDetailsPreview(){
    MobileThemePreview {
        ReservationDetails(
            time = "17:00:00",
            table = "3",
            date = "2024-12-27",
            request = "Lorem ipsum dolor sit amet",
            phone = "0680000000",
            partySize = "5",
            orderCode = ""
        )
    }
}