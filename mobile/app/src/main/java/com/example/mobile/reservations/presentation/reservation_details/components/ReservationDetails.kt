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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            text = "Table: $table",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp
            ),
            text = "Date: ${formatDateTime(date)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
                bottom = 20.dp
            ),
            text = "Time: ${formatTime(time)}",
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