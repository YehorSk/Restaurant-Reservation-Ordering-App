package com.example.mobile.reservations.presentation.create_reservation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.LoadingPart
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.OrderForm
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.reservations.presentation.create_reservation.components.CalendarRoot
import com.example.mobile.reservations.presentation.create_reservation.components.PartySize
import com.example.mobile.reservations.presentation.create_reservation.components.TimeRoot

@Composable
fun CreateReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateReservationViewModel,
    goBack: ()-> Unit,
    goToFinishReservation: () -> Unit,
    orderForm: OrderForm,
    withOrder: Boolean
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAvailableTimeSlots()
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> { goBack() }
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
                onGoBack = {
                    goBack()
                    viewModel.clearForm()
                },
                title = R.string.go_back
            )
            PartySize(
                onPartySizeChanged = { size -> viewModel.updatePartySize(size) },
                partySize = uiState.reservationForm.partySize
            )
            CalendarRoot(
                onUpdateSelectedDate = { date -> viewModel.updateReservationDate(date) }
            )
            if (uiState.timeSlots!=null){
                TimeRoot(
                    date = uiState.reservationForm.reservationDate,
                    slots = uiState.timeSlots!!,
                    selectedSlot = uiState.reservationForm.selectedTimeSlot,
                    onTimeChanged = {id, time ->
                        viewModel.updateTimeSlot(id, time)
                        viewModel.updateWithOrder(withOrder = withOrder, form = orderForm)
                        goToFinishReservation()
                    }
                )
            }
        }
    }else{
        LoadingPart()
    }
}