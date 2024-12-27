package com.example.mobile.reservations.presentation.reservation_details

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.domain.onError
import com.example.mobile.core.domain.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.reservations.data.dao.ReservationDao
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.presentation.OrderBaseViewModel
import com.example.mobile.reservations.data.remote.ReservationRepositoryImpl
import com.example.mobile.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepositoryImpl: ReservationRepositoryImpl,
    reservationDao: ReservationDao
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepositoryImpl, reservationDao){

    fun onAction(action: ReservationDetailsAction){
        when(action){
            is ReservationDetailsAction.CancelReservation -> cancelReservation(action.id)
            is ReservationDetailsAction.GetReservationDetails -> getReservationDetails(action.id)
        }
    }

    fun getReservationDetails(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            reservationRepositoryImpl.getUserReservationDetails(id)
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun cancelReservation(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            reservationRepositoryImpl.cancelUserReservation(id)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}
