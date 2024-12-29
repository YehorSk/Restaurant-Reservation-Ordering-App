package com.example.mobile.orders.presentation.order_details

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepositoryImpl: OrderRepositoryImpl,
    orderDao: OrderDao,
    preferencesRepository: MainPreferencesRepository,
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao, preferencesRepository){

    fun onAction(action: OrderDetailsAction){
        when(action){
            is OrderDetailsAction.UserCancelOrder -> cancelOrder(action.id)
            is OrderDetailsAction.RepeatOrder -> repeatOrder(action.id)
            is OrderDetailsAction.CompleteOrder -> markOrderAsCompleted(action.id)
            is OrderDetailsAction.ConfirmOrder -> markOrderAsConfirmed(action.id)
            is OrderDetailsAction.PrepareOrder -> markOrderAsPreparing(action.id)
            is OrderDetailsAction.ReadyForPickupOrder -> markOrderAsReadyForPickup(action.id)
            is OrderDetailsAction.WaiterCancelOrder -> markOrderAsCancelled(action.id)
        }
    }

    fun getOrderDetails(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.getUserOrderDetails(id)
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun repeatOrder(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.repeatUserOrder(id)
                .onSuccess { data,message ->
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

    fun cancelOrder(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.cancelUserOrder(id)
                .onSuccess { data,message ->
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

    fun markOrderAsCancelled(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.markOrderAsCancelled(id)
                .onSuccess { data,message ->
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

    fun markOrderAsCompleted(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.markOrderAsCompleted(id)
                .onSuccess { data,message ->
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

    fun markOrderAsConfirmed(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.markOrderAsConfirmed(id)
                .onSuccess { data,message ->
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

    fun markOrderAsPreparing(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.markOrderAsPreparing(id)
                .onSuccess { data,message ->
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

    fun markOrderAsReadyForPickup(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepositoryImpl.markOrderAsReadyForPickup(id)
                .onSuccess { data,message ->
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