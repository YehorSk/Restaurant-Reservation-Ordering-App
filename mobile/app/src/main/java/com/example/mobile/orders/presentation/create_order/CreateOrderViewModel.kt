package com.example.mobile.orders.presentation.create_order

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
     networkConnectivityObserver: ConnectivityObserver,
     orderRepositoryImpl: OrderRepositoryImpl
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl){


    fun updateOrderType(type: Int, text: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                                    orderType = type,
                                    orderText = text
                    )
                )
        }
    }

    fun updateRequest(request: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    specialRequest = request
                )
            )
        }
    }

    fun updateAddress(address: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    address = address
                )
            )
        }
    }

    fun updateInstructions(instructions: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    instructions = instructions
                )
            )
        }
    }

    fun validateForm(): Boolean{
        with(_uiState.value.orderForm){
            return if(orderType == 0){
                true
            }else if(orderType == 1){
                instructions.isNotBlank() && address.isNotBlank()
            }else if(orderType == 2){
                true
            }else{
                false
            }
        }
    }

    fun makeOrder(){
        with(_uiState.value.orderForm){
            if(orderType == 0){
                makePickupOrder()
            }else if(orderType == 1){
                makeDeliveryOrder()
            }else if(orderType == 2){
                null
            }else{
                null
            }
        }
    }

    fun getUserOrderItems(){
        Timber.d("getUserOrderItems")
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if(available){
                    val result = orderRepositoryImpl.getUserOrderItems()
                    when(result){
                        is NetworkResult.Error ->{
                            _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                        }
                        is NetworkResult.Success ->{
                            _uiState.update {
                                it.copy(items = result.data)
                            }
                        }
                    }
                } else {
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
            }
        }
    }

    fun makePickupOrder(){
        Timber.d("makePickupOrder")
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if(available){
                    val result = orderRepositoryImpl.makeUserPickUpOrder(uiState.value.orderForm)
                    when(result){
                        is NetworkResult.Error ->{
                            _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                        }
                        is NetworkResult.Success ->{
                            _sideEffectChannel.send(SideEffect.ShowToast("Pickup order was created"))
                            _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                        }
                    }
                } else {
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
            }
        }
    }

    fun makeDeliveryOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if(available){
                    val result = orderRepositoryImpl.makeUserDeliveryOrder(uiState.value.orderForm)
                    when(result){
                        is NetworkResult.Error ->{
                            _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                        }
                        is NetworkResult.Success ->{
                            _sideEffectChannel.send(SideEffect.ShowToast("Delivery order was created"))
                            _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                        }
                    }
                } else {
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
            }
        }
    }


}