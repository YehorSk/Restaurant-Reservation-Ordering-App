package com.yehorsk.platea.orders.presentation.order_details

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.remote.OrderRepositoryImpl
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepository: OrderRepository,
    orderDao: OrderDao,
    preferencesRepository: MainPreferencesRepository,
    restaurantInfoDao: RestaurantInfoDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepository, orderDao, preferencesRepository, restaurantInfoDao){

    fun onAction(action: OrderDetailsAction){
        when(action){
            is OrderDetailsAction.UserCancelOrder -> cancelOrder(action.id)
            is OrderDetailsAction.SetCompletedStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetConfirmedStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetPreparingStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetReadyForPickupStatus -> updateOrderStatus(action.id, action.status)
            is OrderDetailsAction.SetCancelledStatus -> updateOrderStatus(action.id, action.status)
        }
    }

    fun getOrderDetails(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            orderRepository.getUserOrderDetails(id)
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