package com.example.mobile.reservations.presentation.reservation_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.core.utils.formatDateTime
import com.example.mobile.core.utils.formatTime
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun ReservationDetails(
    modifier: Modifier = Modifier,
    time: String,
    table: String,
    date: String
){
    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp
            ),
            text = stringResource(R.string.table, table),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp
            ),
            text = stringResource(R.string.date_reservation, formatDateTime(date)),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
                bottom = 20.dp
            ),
            text = stringResource(R.string.time_reservation, formatTime(time)),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun ReservationDetailsPreview(){
    MobileTheme {
        ReservationDetails(
            time = "17:00:00",
            table = "3",
            date = "2024-12-27"
        )
    }
}