package com.example.mobile.reservations.presentation.create_reservation

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.reservations.data.dao.ReservationDao
import com.example.mobile.reservations.data.remote.ReservationRepositoryImpl
import com.example.mobile.reservations.presentation.ReservationBaseViewModel
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
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
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
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

}