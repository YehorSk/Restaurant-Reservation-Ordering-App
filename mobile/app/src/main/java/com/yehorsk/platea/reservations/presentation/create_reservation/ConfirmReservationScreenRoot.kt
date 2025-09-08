package com.yehorsk.platea.reservations.presentation.create_reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.PhoneInput
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.reservations.presentation.create_reservation.components.ConfirmReservationDetails
import com.yehorsk.platea.reservations.presentation.create_reservation.components.ReservationSpecialRequest
import com.yehorsk.platea.reservations.presentation.reservations.ReservationUiState

@Composable
fun ConfirmReservationScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CreateReservationViewModel,
    goBack: ()->Unit,
    goBackToMenu: () -> Unit
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> { goBackToMenu() }
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
            is SideEffect.LanguageChanged -> {}
        }
    }

    ConfirmReservationScreen(
        modifier = modifier,
        uiState = uiState,
        goBack = { goBack() },
        onAction = viewModel::onAction
    )

}

@Composable
fun ConfirmReservationScreen(
    modifier: Modifier = Modifier,
    uiState: CreateReservationUiState,
    goBack: ()->Unit,
    onAction: (CreateReservationAction) -> Unit
){

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
                onRequestChange = { onAction(CreateReservationAction.UpdateSpecialRequest(it)) }
            )
            PhoneInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                phone = uiState.phone,
                code = uiState.countryCode,
                onFullPhoneChanged = { onAction(CreateReservationAction.UpdatePhone(it)) },
                onPhoneValidated = { onAction(CreateReservationAction.ValidatePhoneNumber(it)) },
                showText = true
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
                enabled = uiState.isPhoneValid,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                onClick = { onAction(CreateReservationAction.CreateReservation) }
            ) {
                Text(
                    text = stringResource(R.string.book),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }else{
        LoadingPart()
    }
}