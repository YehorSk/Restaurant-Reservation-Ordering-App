package com.example.mobile.reservations.presentation.reservation_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobile.R
import com.example.mobile.core.presentation.components.ActionButton

@Composable
fun ReservationActions(
    modifier: Modifier = Modifier,
    onCancelReservation:() -> Unit,
){

    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {
        ActionButton(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 5.dp
                ),
            onAction = onCancelReservation,
            color = MaterialTheme.colorScheme.error,
            text = R.string.cancel_reservation
        )
    }

}