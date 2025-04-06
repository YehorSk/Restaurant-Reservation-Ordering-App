package com.yehorsk.platea.reservations.presentation.create_reservation

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.Utility.getSchedule
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.reservations.presentation.create_reservation.components.CalendarRoot
import com.yehorsk.platea.reservations.presentation.create_reservation.components.PartySize
import com.yehorsk.platea.reservations.presentation.create_reservation.components.TimeRoot
import com.yehorsk.platea.reservations.presentation.reservations.ReservationUiState
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CreateReservationScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CreateReservationViewModel,
    goBack: ()-> Unit,
    goToFinishReservation: () -> Unit,
    orderForm: OrderForm,
    withOrder: Boolean
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val restaurantInfoUiState by viewModel.restaurantInfoUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAvailableTimeSlots()
        viewModel.getMaxCapacity()
    }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> { goBack() }
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
        }
    }

    CreateReservationScreen(
        modifier = modifier,
        goBack = goBack,
        uiState = uiState,
        goToFinishReservation = { goToFinishReservation() },
        orderForm = orderForm,
        withOrder = withOrder,
        onAction = viewModel::onAction,
        schedule = restaurantInfoUiState
    )
}

@Composable
fun CreateReservationScreen(
    modifier: Modifier = Modifier,
    goBack: ()-> Unit,
    uiState: ReservationUiState,
    goToFinishReservation: () -> Unit,
    orderForm: OrderForm,
    withOrder: Boolean,
    schedule: RestaurantInfoEntity?,
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
                onGoBack = {
                    goBack()
                    onAction(CreateReservationAction.ClearForm)
                },
                title = R.string.go_back
            )
            if(uiState.maxCapacity != null){
                PartySize(
                    onPartySizeChanged = { onAction(CreateReservationAction.UpdatePartySize(it)) },
                    partySize = uiState.reservationForm.partySize,
                    maxTableSize = uiState.maxCapacity
                )
            }
            if(schedule != null){
                val formattedSchedule = getSchedule(schedule.openingHours)

                val closedDays = formattedSchedule.filter {
                    !it.value.isOpen
                }.map { it.key }.toTypedArray()

                CalendarRoot(
                    onUpdateSelectedDate = { onAction(CreateReservationAction.UpdateReservationDate(it)) },
                    selectedDate = uiState.reservationForm.reservationDate,
                    closedDays = closedDays
                )

                if (uiState.timeSlots!=null){
                    val isClosed = LocalDate.parse(uiState.reservationForm.reservationDate).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH) in closedDays
                    TimeRoot(
                        date = uiState.reservationForm.reservationDate,
                        slots = uiState.timeSlots,
                        selectedSlot = uiState.reservationForm.selectedTimeSlot,
                        onTimeChanged = {id, time ->
                            onAction(CreateReservationAction.UpdateTimeSlot(id, time))
                            onAction(CreateReservationAction.UpdateWithOrder(withOrder, orderForm))
                            goToFinishReservation()
                        },
                        isClosed = isClosed
                    )
                }
            }
        }
    }else{
        LoadingPart()
    }
}