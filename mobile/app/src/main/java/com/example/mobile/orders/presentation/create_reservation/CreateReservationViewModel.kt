package com.example.mobile.orders.presentation.create_reservation

import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.domain.onError
import com.example.mobile.core.domain.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.dao.ReservationDao
import com.example.mobile.orders.data.db.model.ReservationEntity
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.data.remote.dto.toReservationEntity
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateReservationViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepositoryImpl: OrderRepositoryImpl,
    orderDao: OrderDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao){

    fun updatePartySize(size: Int){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    partySize = size
                )
            )
        }
        getAvailableTimeSlots()
    }

    fun updateReservationDate(date: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    reservationDate = date
                )
            )
        }
        getAvailableTimeSlots()
    }

    fun updateTimeSlot(slot: Int){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    selectedTimeSlot = slot
                )
            )
        }
    }

    fun getAvailableTimeSlots(){
        Timber.d("getAvailableTimeSlots")
        viewModelScope.launch{
            orderRepositoryImpl.getAvailableTimeSlots(uiState.value.orderForm)
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

    fun createReservation(){
        Timber.d("createReservation")
        viewModelScope.launch{
            orderRepositoryImpl.createReservation(uiState.value.orderForm)
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