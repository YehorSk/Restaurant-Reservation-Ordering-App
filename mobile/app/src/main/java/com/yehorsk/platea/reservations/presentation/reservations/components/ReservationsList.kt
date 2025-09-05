package com.yehorsk.platea.reservations.presentation.reservations.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.core.presentation.components.ListHeader
import com.yehorsk.platea.reservations.domain.models.SectionedReservations
import kotlin.collections.List

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReservationsList(
    modifier: Modifier = Modifier,
    items: List<SectionedReservations>,
    onGoToReservationDetails: (Int) -> Unit,
    showStatus: Boolean = false
){

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items.forEachIndexed{ index, (date, reservations) ->
            stickyHeader{
                ListHeader(
                    title = date.asString()
                )
            }
            itemsIndexed(reservations, key = { _, reservation -> reservation.id }){ index, item ->
                ReservationItem(
                    reservationEntity = item,
                    onGoToReservationDetails = { onGoToReservationDetails(it) },
                    showStatus = showStatus
                )
                HorizontalDivider()
            }
        }
    }
}