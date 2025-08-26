package com.yehorsk.platea.reservations.presentation.reservations.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yehorsk.platea.core.presentation.components.ListHeader
import com.yehorsk.platea.core.utils.Utility.SectionedReservation
import com.yehorsk.platea.core.utils.Utility.groupReservationsByDate
import com.yehorsk.platea.reservations.domain.models.Reservation
import kotlin.collections.List

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReservationsList(
    modifier: Modifier = Modifier,
    items: List<Reservation>,
    onGoToReservationDetails: (Int) -> Unit,
    showStatus: Boolean = false
){

    var reservationsGrouped by remember { mutableStateOf<List<SectionedReservation>>(emptyList()) }
    LaunchedEffect(items) {
        reservationsGrouped = groupReservationsByDate(items)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        reservationsGrouped.forEach{ group ->
            stickyHeader{
                ListHeader(
                    title = group.title
                )
            }
            items(group.reservations, key = { it.id }){ item ->
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