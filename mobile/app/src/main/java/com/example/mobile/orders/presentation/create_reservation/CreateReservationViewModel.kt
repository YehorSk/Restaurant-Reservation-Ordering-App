package com.example.mobile.orders.presentation.create_reservation

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.domain.onError
import com.example.mobile.core.domain.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
    }

    fun updateReservationDate(date: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    reservationDate = date
                )
            )
        }
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
            setLoadingState(true)
            orderRepositoryImpl.getAvailableTimeSlots()
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(timeSlots = data)
                    }
                    setLoadingState(false)
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                    setLoadingState(false)
                }
        }
    }

}