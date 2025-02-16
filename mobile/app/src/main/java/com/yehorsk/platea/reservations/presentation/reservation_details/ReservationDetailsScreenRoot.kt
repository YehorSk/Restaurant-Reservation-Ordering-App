package com.yehorsk.platea.reservations.presentation.reservation_details

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.presentation.components.ActionButton
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.toString
import com.yehorsk.platea.orders.presentation.components.NavBar
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.presentation.reservation_details.components.ReservationDetails
import com.yehorsk.platea.reservations.presentation.reservation_details.components.ReservationStatus
import com.yehorsk.platea.reservations.presentation.reservations.ReservationUiState

@Composable
fun ReservationDetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ReservationDetailsViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    id: Int
){

    val context = LocalContext.current
    val reservationItemUiState by viewModel.reservationItemUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
            is SideEffect.NavigateToNextScreen -> { onGoBack() }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getReservationDetails(id.toString())
    }

    ReservationDetailsScreen(
        modifier = modifier,
        onGoBack = onGoBack,
        id = id,
        isConnected = isConnected,
        onAction = viewModel::onAction,
        uiState = uiState,
        reservationItemUiState = reservationItemUiState,
        userRole = userRole.toString()
    )

}

@Composable
fun ReservationDetailsScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    id: Int,
    isConnected: Boolean,
    onAction: (ReservationDetailsAction) -> Unit,
    uiState: ReservationUiState,
    reservationItemUiState: List<ReservationEntity>,
    userRole: String
){
    val data = reservationItemUiState.find { it.id.toString() == id.toString() }

    if(uiState.isLoading){
        LoadingPart()
    }else if(data!=null){

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
                status = data.status,
                date = data.createdAt,
                code = data.code
            )
            HorizontalDivider()
            ReservationDetails(
                time = data.startTime,
                table = data.tableNumber,
                date = data.date,
                request = data.specialRequest,
                phone = data.phone,
                partySize = data.partySize.toString()
            )
            if(isConnected){
                if(userRole == "user"){
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
                        enabled = data.status != "Cancelled"
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
                            enabled = data.status != "Pending"
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
                            enabled = data.status != "Confirmed"
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
                            enabled = data.status != "Cancelled"
                        )
                    }
                }
            }
        }
    }
}