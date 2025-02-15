package com.yehorsk.platea.reservations.presentation.create_reservation

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
import com.yehorsk.platea.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateReservationViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepositoryImpl: ReservationRepositoryImpl,
    reservationDao: ReservationDao,
    preferencesRepository: MainPreferencesRepository
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepositoryImpl, reservationDao, preferencesRepository){

    fun updatePartySize(size: Int){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    partySize = size
                )
            )
        }
        getAvailableTimeSlots()
    }

    fun updateReservationDate(date: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    reservationDate = date
                )
            )
        }
        getAvailableTimeSlots()
    }

    fun updateSpecialRequest(request: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    specialRequest = request
                )
            )
        }
    }

    fun updatePhone(phone: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    phone = phone
                )
            )
        }
    }


    fun updateTimeSlot(slot: Int, time: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    selectedTimeSlot = slot,
                    selectedTime = time
                )
            )
        }
    }

    fun updateWithOrder(withOrder: Boolean, form: OrderForm){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    orderForm = form,
                    withOrder = withOrder
                )
            )
        }
    }

    fun getAvailableTimeSlots(){
        Timber.d("getAvailableTimeSlots")
        viewModelScope.launch{
            reservationRepositoryImpl.getAvailableTimeSlots(uiState.value.reservationForm)
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(timeSlots = data)
                    }
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

    fun validatePhoneNumber(): Boolean{
        val phoneRegex = "^[+]?[0-9]{10,15}$"
        return _uiState.value.reservationForm.phone.matches(phoneRegex.toRegex())
    }

    fun createReservation(){
        Timber.d("createReservation")
        viewModelScope.launch{
            reservationRepositoryImpl.createReservation(uiState.value.reservationForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                    clearForm()
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

}