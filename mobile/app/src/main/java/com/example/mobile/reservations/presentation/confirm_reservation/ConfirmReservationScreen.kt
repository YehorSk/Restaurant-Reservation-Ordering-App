package com.example.mobile.reservations.presentation.confirm_reservation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.LoadingPart
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.reservations.presentation.confirm_reservation.components.ConfirmReservationDetails
import com.example.mobile.reservations.presentation.confirm_reservation.components.ReservationPhoneInput
import com.example.mobile.reservations.presentation.confirm_reservation.components.ReservationSpecialRequest
import com.example.mobile.reservations.presentation.create_reservation.CreateReservationViewModel

@Composable
fun ConfirmReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateReservationViewModel,
    goBack: ()->Unit,
    goBackToMenu: () -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> { goBackToMenu() }
        }
    }

    if(!uiState.isLoading){
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavBar(
                onGoBack = goBack,
                title = R.string.go_back
            )
            ConfirmReservationDetails(
                partySize = uiState.reservationForm.partySize.toString(),
                reservationDate = uiState.reservationForm.reservationDate,
                time = uiState.reservationForm.selectedTime
            )
            ReservationSpecialRequest(
                request = uiState.reservationForm.specialRequest,
                onRequestChange = { viewModel.updateSpecialRequest(it) }
            )
            ReservationPhoneInput(
                phone = uiState.reservationForm.phone,
                onPhoneChanged = { viewModel.updatePhone(it) }
            )
            Button(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp,
                        bottom = 10.dp
                    )
                    .fillMaxWidth(),
                enabled = viewModel.validatePhoneNumber(),
                onClick = {
                    viewModel.createReservation()
                }
            ) {
                Text(
                    text = stringResource(R.string.book),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }else{
        LoadingPart()
    }

}