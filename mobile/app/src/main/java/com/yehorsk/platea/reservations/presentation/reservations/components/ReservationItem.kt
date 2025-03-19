package com.yehorsk.platea.reservations.presentation.reservations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.core.utils.Utility.statusToString
import com.yehorsk.platea.core.utils.formatOrderDateTime
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity

@Composable
fun ReservationItem(
    modifier: Modifier = Modifier,
    reservationEntity: ReservationEntity,
    onGoToReservationDetails: (Int) -> Unit
){
    val context = LocalContext.current
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable {
                onGoToReservationDetails(reservationEntity.id)
            }
    ){
        Column {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = reservationEntity.code,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            ) {
                Text(
                    text = formatOrderDateTime(reservationEntity.createdAt),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                )
                Text(
                    text = " ${statusToString(reservationEntity.status, context)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}