package com.yehorsk.platea.orders.presentation.order_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.navigation.Screen
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.data.remote.dto.toOrder
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val orderRepository: OrderRepository,
    val preferencesRepository: MainPreferencesRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val orderId = savedStateHandle.toRoute<Screen.OrderDetails>().id

    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeOrderDetails()
        observeNetwork()
        observeUserRole()
    }


    fun onAction(action: OrderDetailsAction){
        when(action){
            is OrderDetailsAction.UserCancelOrder -> cancelOrder(action.id)
            is OrderDetailsAction.SetCompletedStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetConfirmedStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetPreparingStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetReadyForPickupStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetCancelledStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.OnGetOrderById -> getOrderDetails(action.id)
        }
    }

    private fun observeNetwork(){
        networkConnectivityObserver.observe()
            .onEach { network ->
                _uiState.update { it.copy(
                    isNetwork = network
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeUserRole(){
        preferencesRepository.userRoleFlow
            .onEach { role ->
                _uiState.update { it.copy(
                    userRole = role
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeOrderDetails(){
        orderRepository.getUserOrderDetailsFlow(orderId)
            .onEach { order ->
                _uiState.update { it.copy(
                    currentOrder = order
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun getOrderDetails(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepository.getUserOrderDetails(id)
                .onSuccess { data,message ->
                    _uiState.update {
                        it.copy(currentOrder = data.first())
                    }
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
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
            orderRepository.cancelUserOrder(id)
                .onSuccess { data,message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _uiState.update {
                        it.copy(currentOrder = data.first().toOrder())
                    }
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }


    fun updateOrderStatus(id: String, status: Status) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepository.updateOrderStatus(id, status)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _uiState.update {
                        it.copy(currentOrder = data.first().toOrder())
                    }
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

}