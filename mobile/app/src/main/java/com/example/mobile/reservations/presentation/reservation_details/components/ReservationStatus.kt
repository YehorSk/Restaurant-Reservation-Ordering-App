package com.example.mobile.reservations.presentation.reservation_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.utils.formatOrderDateTime
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun ReservationStatus(
    modifier: Modifier = Modifier,
    status: String,
    date: String,
    code: String
){
    Card(
    ) {
        Column {
            Row(
                modifier = modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 15.dp
                    ),
                    text = "Reservation #$code",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                modifier = modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 15.dp,
                        bottom = 10.dp
                    ),
                    text = status,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 15.dp,
                        bottom = 10.dp
                    ),
                    text = formatOrderDateTime(date),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
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