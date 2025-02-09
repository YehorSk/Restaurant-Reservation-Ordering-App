package com.yehorsk.platea.orders.presentation.create_order

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.remote.OrderRepositoryImpl
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.domain.repository.GooglePlacesRepository
import com.yehorsk.platea.orders.presentation.OrderBaseViewModel
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
            CreateOrderAction.CloseBottomSheet -> closeBottomSheet()
            CreateOrderAction.OpenBottomSheet -> showBottomSheet()
            is CreateOrderAction.UpdateTime -> updateTime(start = action.start, end = action.end)
        }
    }

    fun showBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = true)
        }
    }

    fun closeBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = false)
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

    fun updateTime(start: String, end: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    startTime = start,
                    endTime = end
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
//        getPlaces(address)
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
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

    fun makePickupOrder(){
        Timber.d("makePickupOrder")
        viewModelScope.launch{
            orderRepositoryImpl.makeUserPickUpOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
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

    fun makeDeliveryOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
            orderRepositoryImpl.makeUserDeliveryOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
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

    fun makeWaiterOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
            orderRepositoryImpl.makeWaiterOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
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
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                    setLoadingState(false)
                }
        }
    }

}