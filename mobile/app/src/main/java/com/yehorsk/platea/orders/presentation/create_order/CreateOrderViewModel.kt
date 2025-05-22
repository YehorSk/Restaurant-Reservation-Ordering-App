package com.yehorsk.platea.orders.presentation.create_order

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.Utility.getEndTime
import com.yehorsk.platea.core.utils.Utility.getStartTime
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepository: OrderRepository,
    preferencesRepository: MainPreferencesRepository,
    restaurantInfoDao: RestaurantInfoDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepository, preferencesRepository, restaurantInfoDao){

    val restaurantInfoUiState: StateFlow<RestaurantInfoEntity?> = restaurantInfoDao.getRestaurantInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val cartItemsUiState: StateFlow<List<CartItemEntity>> = orderRepository.getAllCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

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
            is CreateOrderAction.MakeWaiterOrder -> makeWaiterOrder()
            CreateOrderAction.CloseBottomSheet -> closeBottomSheet()
            CreateOrderAction.OpenBottomSheet -> showBottomSheet()
            is CreateOrderAction.UpdateTime -> updateTime(start = action.start, end = action.end)
            is CreateOrderAction.UpdateDate -> updateDate(date = action.date)
            is CreateOrderAction.UpdatePhone -> updatePhone(phone = action.phone)
            is CreateOrderAction.ValidatePhone -> validatePhoneNumber(action.phone)
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

    fun validatePhoneNumber(phone: String){
        val phoneRegex = "^[+]?[0-9]{10,15}$"
        _uiState.update { currentState ->
            currentState.copy(
                isPhoneValid = phone.matches(phoneRegex.toRegex())
            )
        }
    }

    fun updatePhone(phone: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    fullPhone = phone
                )
            )
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

    fun updateDate(date: String){
        Timber.d("updateDate: $date")
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    selectedDate = date
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

    fun validateForm(): Boolean{
        with(_uiState.value.orderForm){
            return if(orderType == 0){
                true
            }else if(orderType == 1){
                instructions.isNotBlank() && address.isNotBlank() && _uiState.value.isPhoneValid
            }else if(orderType == 2){
                true
            }else if(orderType == 3){
                _uiState.value.orderForm.selectedTable != null
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
            orderRepository.getUserOrderItems()
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
            orderRepository.makeUserPickUpOrder(uiState.value.orderForm)
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
            orderRepository.makeUserDeliveryOrder(uiState.value.orderForm)
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
        Timber.d("makeWaiterOrder")
        viewModelScope.launch{
            _uiState.update { state ->
                state.copy(
                    orderForm = _uiState.value.orderForm.copy(
                        startTime = getStartTime(),
                        endTime = getEndTime()
                    )
                )
            }
            orderRepository.makeWaiterOrder(uiState.value.orderForm)
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
            orderRepository.getTables()
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