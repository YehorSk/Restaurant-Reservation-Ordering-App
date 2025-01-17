package com.example.mobile.reservations.presentation.confirm_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
 import com.example.mobile.R
import com.example.mobile.core.utils.formatDateTime
import com.example.mobile.core.utils.formatTime
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun ConfirmReservationDetails(
    partySize: String,
    reservationDate: String,
    time: String
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(
                    bottom = 15.dp
                ),
            text = stringResource(R.string.platea_restaurant),
            color = contentColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Row {
            Icon(
                modifier = Modifier
                    .padding(
                        end = 5.dp
                    ),
                tint = contentColor,
                imageVector = Icons.Filled.SupervisorAccount,
                contentDescription = ""
            )
            Text(
                text = partySize,
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge,
            )
            Icon(
                modifier = Modifier
                    .padding(
                        end = 5.dp,
                        start = 20.dp
                    ),
                tint = contentColor,
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = ""
            )
            Text(
                text = formatDateTime(reservationDate),
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier
                    .padding(
                        end = 5.dp,
                        start = 5.dp
                    ),
                text = stringResource(R.string.at, formatTime(time)),
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ConfirmReservationDetailsPreview(){
    MobileTheme {
        ConfirmReservationDetails(
            partySize = "2",
            reservationDate = "2024-12-20",
            time = "08:00:00"
        )
    }
}