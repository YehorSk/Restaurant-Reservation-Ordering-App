package com.example.mobile.orders.presentation.create_order

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
     networkConnectivityObserver: ConnectivityObserver,
     orderRepositoryImpl: OrderRepositoryImpl,
     orderDao: OrderDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao){

    fun onAction(action: CreateOrderAction){
        when(action){
            CreateOrderAction.MakeOrder -> makeOrder()
            is CreateOrderAction.UpdateAddress -> updateAddress(action.address)
            is CreateOrderAction.UpdateInstructions -> updateInstructions(action.instructions)
            is CreateOrderAction.UpdateOrderType -> updateOrderType(
                type = action.type,
                text = action.text
            )
            is CreateOrderAction.UpdateRequest -> updateRequest(action.request)
        }
    }

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

    fun updateTableNumber(number: Int){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    tableNumber = number
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
        }
    }

    fun makePickupOrder(){
        Timber.d("makePickupOrder")
        viewModelScope.launch{
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
        }
    }

    fun makeDeliveryOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
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
        }
    }


}