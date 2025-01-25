package com.example.mobile.orders.presentation.create_order

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.remote.Result
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.data.remote.dto.TableDto
import com.example.mobile.orders.domain.repository.GooglePlacesRepository
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
     networkConnectivityObserver: ConnectivityObserver,
     orderRepositoryImpl: OrderRepositoryImpl,
     orderDao: OrderDao,
     preferencesRepository: MainPreferencesRepository,
     private val googlePlacesRepository: GooglePlacesRepository
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao, preferencesRepository){

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
            is CreateOrderAction.UpdatePlace -> updatePlace(action.place)
            is CreateOrderAction.MakeWaiterOrder -> makeWaiterOrder()
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

    fun updateTableNumber(table: TableDto){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    selectedTable = table
                )
            )
        }
    }

    fun updatePlace(address: String){
        getPlaces(address)
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
            if(orderType == 0){ // Pickup User
                makePickupOrder()
            }else if(orderType == 1){ // Delivery User
                makeDeliveryOrder()
            }else{
                null
            }
        }
    }

    fun getUserOrderItems(){
        Timber.d("getUserOrderItems")
        viewModelScope.launch{
            orderRepositoryImpl.getUserOrderItems()
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(orderItems = data)
                    }
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

    fun makePickupOrder(){
        Timber.d("makePickupOrder")
        viewModelScope.launch{
            orderRepositoryImpl.makeUserPickUpOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

    fun makeDeliveryOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
            orderRepositoryImpl.makeUserDeliveryOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

    fun makeWaiterOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
            orderRepositoryImpl.makeWaiterOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

    fun getTables(){
        Timber.d("getTables")
        viewModelScope.launch{
            setLoadingState(true)
            orderRepositoryImpl.getTables()
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(tables = data)
                    }
                    setLoadingState(false)
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                    setLoadingState(false)
                }
        }
    }

    fun getPlaces(address: String){
        Timber.d("getPlaces")
        viewModelScope.launch{
            val response = googlePlacesRepository.getPredictions(address)
            setPlacesLoadingState(true)
            when(response){
                is Result.Success -> {
                    _uiState.update {
                        it.copy(places = response.data.predictions)
                    }
                    Timber.d("Prediction: " + response.data.predictions)
                    setPlacesLoadingState(false)
                }
                is Result.Error -> {
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(response.error))
                    setPlacesLoadingState(false)
                }
            }
        }
    }

}