package com.yehorsk.platea.reservations.presentation.reservations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity

@Composable
fun ReservationsList(
    modifier: Modifier = Modifier,
    items: List<ReservationEntity>,
    onGoToReservationDetails: (Int) -> Unit
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(items){ reservation ->
            ReservationItem(
                reservationEntity = reservation,
                onGoToReservationDetails = { onGoToReservationDetails(it) }
            )
            HorizontalDivider()
        }
    }
}