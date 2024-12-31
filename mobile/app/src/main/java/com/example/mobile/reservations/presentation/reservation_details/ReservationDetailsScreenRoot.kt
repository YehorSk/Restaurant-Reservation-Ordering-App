package com.example.mobile.reservations.presentation.reservation_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.ActionButton
import com.example.mobile.core.presentation.components.LoadingPart
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.reservations.data.db.model.ReservationEntity
import com.example.mobile.reservations.presentation.reservation_details.components.ReservationDetails
import com.example.mobile.reservations.presentation.reservation_details.components.ReservationStatus
import com.example.mobile.reservations.presentation.reservations.CreateReservationUiState

@Composable
fun ReservationDetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ReservationDetailsViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    id: Int
){

    val reservationItemUiState by viewModel.reservationItemUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val context = LocalContext.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
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
        reservationItemUiState = reservationItemUiState
    )

}

@Composable
fun ReservationDetailsScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    id: Int,
    isConnected: Boolean,
    onAction: (ReservationDetailsAction) -> Unit,
    uiState: CreateReservationUiState,
    reservationItemUiState: List<ReservationEntity>
){
    val data = reservationItemUiState.find { it.id.toString() == id.toString() }

    if(uiState.isLoading){
        LoadingPart()
    }else if(data!=null){

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
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
                date = data.date
            )
            if(isConnected){
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
            }
        }
    }
}