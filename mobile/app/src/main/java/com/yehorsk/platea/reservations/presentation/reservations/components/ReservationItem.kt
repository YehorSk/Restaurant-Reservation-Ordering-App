package com.yehorsk.platea.reservations.presentation.reservations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.core.utils.Utility.statusToString
import com.yehorsk.platea.core.utils.formatOrderDateTime
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.domain.models.Reservation

@Composable
fun ReservationItem(
    modifier: Modifier = Modifier,
    reservationEntity: Reservation,
    onGoToReservationDetails: (Int) -> Unit,
    showStatus: Boolean = false
){
    val context = LocalContext.current
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .clickable {
                onGoToReservationDetails(reservationEntity.id)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = reservationEntity.code,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            ) {
                Text(
                    text = formatOrderDateTime(reservationEntity.createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                )
                Text(
                    text = " ${statusToString(reservationEntity.status, context)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}