package com.example.mobile.orders.presentation.reservations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobile.orders.data.db.model.ReservationEntity
import com.example.mobile.orders.presentation.orders.components.OrderListItem

@Composable
fun ReservationsList(
    modifier: Modifier = Modifier,
    items: List<ReservationEntity>
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(items){ reservation ->
            ReservationItem(
                reservationEntity = reservation,
            )
            HorizontalDivider()
        }
    }
}