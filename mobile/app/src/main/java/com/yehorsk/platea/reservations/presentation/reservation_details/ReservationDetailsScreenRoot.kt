package com.yehorsk.platea.reservations.presentation.reservation_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.ActionButton
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.reservations.presentation.reservation_details.components.ReservationDetails
import com.yehorsk.platea.reservations.presentation.reservation_details.components.ReservationStatus

@Composable
fun ReservationDetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ReservationDetailsViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    id: Int
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getReservationDetails(id.toString())
    }

    ReservationDetailsScreen(
        modifier = modifier,
        onGoBack = onGoBack,
        id = id,
        onAction = viewModel::onAction,
        uiState = uiState,
    )

}

@Composable
fun ReservationDetailsScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    id: Int,
    onAction: (ReservationDetailsAction) -> Unit,
    uiState: ReservationDetailsUiState,
){

    if(uiState.isLoading){
        LoadingPart()
    }else if(uiState.currentReservation!=null){

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            NavBar(
                onGoBack = onGoBack,
                title = R.string.go_back
            )
            ReservationStatus(
                status = uiState.currentReservation.status,
                date = uiState.currentReservation.createdAt,
                code = uiState.currentReservation.code
            )
            HorizontalDivider()
            ReservationDetails(
                time = uiState.currentReservation.startTime,
                table = uiState.currentReservation.tableNumber,
                date = uiState.currentReservation.date,
                request = uiState.currentReservation.specialRequest,
                phone = uiState.currentReservation.phone,
                partySize = uiState.currentReservation.partySize.toString(),
                orderCode = uiState.currentReservation.orderCode
            )
            if(uiState.isNetwork){
                if(uiState.userRole == "user"){
                    ActionButton(
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 10.dp,
                                bottom = 5.dp
                            ),
                        onAction = { onAction(ReservationDetailsAction.CancelReservation(id.toString())) },
                        text = R.string.cancel_reservation,
                        enabled = uiState.currentReservation.status != "Cancelled"
                    )
                }else{
                    Column(
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.background)
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
                            onAction = { onAction(ReservationDetailsAction.SetPendingStatus(id.toString())) },
                            text = R.string.pending_reservation,
                            enabled = uiState.currentReservation.status != "Pending"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 10.dp,
                                    bottom = 5.dp
                                ),
                            onAction = { onAction(ReservationDetailsAction.SetConfirmedStatus(id.toString())) },
                            text = R.string.confirm_reservation,
                            enabled = uiState.currentReservation.status != "Confirmed"
                        )
                        ActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 10.dp,
                                    bottom = 5.dp
                                ),
                            onAction = { onAction(ReservationDetailsAction.SetCancelledStatus(id.toString())) },
                            text = R.string.cancel_reservation,
                            enabled = uiState.currentReservation.status != "Cancelled"
                        )
                    }
                }
            }
        }
    }
}