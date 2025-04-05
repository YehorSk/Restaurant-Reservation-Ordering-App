package com.yehorsk.platea.reservations.presentation.reservations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.core.presentation.components.ListHeader
import com.yehorsk.platea.core.utils.Utility.groupReservationsByDate
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity

@Composable
fun ReservationsList(
    modifier: Modifier = Modifier,
    items: List<ReservationEntity>,
    onGoToReservationDetails: (Int) -> Unit,
    showStatus: Boolean = false
){

    val reservationsGrouped = groupReservationsByDate(items)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        itemsIndexed(reservationsGrouped, key = { _, group -> group.title }){ _, group ->
            ListHeader(
                title = group.title
            )
            var i = 0;
            Column {
                group.reservations.forEach{ reservation ->
                    ReservationItem(
                        reservationEntity = reservation,
                        onGoToReservationDetails = { onGoToReservationDetails(it) },
                        showStatus = showStatus
                    )
                    if(i++ != group.reservations.size -1) HorizontalDivider()
                }
            }
        }
    }
}