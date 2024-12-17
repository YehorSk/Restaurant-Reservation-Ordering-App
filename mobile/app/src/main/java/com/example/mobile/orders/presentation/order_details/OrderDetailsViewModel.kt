package com.example.mobile.orders.presentation.order_details

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
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
    orderDao: OrderDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao){

    fun onAction(action: OrderDetailsAction){
        when(action){
            is OrderDetailsAction.CancelOrder -> cancelOrder(action.id)
            is OrderDetailsAction.RepeatOrder -> repeatOrder(action.id)
        }
    }

    fun getOrderDetails(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = orderRepositoryImpl.getUserOrderDetails(id)
            when(result){
                is NetworkResult.Error ->{
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                }
                is NetworkResult.Success ->{
                }
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
            val result = orderRepositoryImpl.repeatUserOrder(id)
            when(result){
                is NetworkResult.Error ->{
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                }
                is NetworkResult.Success ->{
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                }
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
            val result = orderRepositoryImpl.cancelUserOrder(id)
            when(result){
                is NetworkResult.Error ->{
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                }
                is NetworkResult.Success ->{
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

}