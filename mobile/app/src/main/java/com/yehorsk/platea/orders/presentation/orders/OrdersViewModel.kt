package com.yehorsk.platea.orders.presentation.orders

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepository: OrderRepository,
    preferencesRepository: MainPreferencesRepository,
    restaurantInfoDao: RestaurantInfoDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepository, preferencesRepository, restaurantInfoDao){

    private val _filterOption = MutableStateFlow(OrderFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val orderItemsUiState: StateFlow<List<Order>> = combine(_filterOption, _searchText) { filter, search ->
            when (filter) {
                OrderFilter.COMPLETED -> orderRepository.getUserOrders(search, "Completed")
                OrderFilter.PENDING -> orderRepository.getUserOrders(search, "Pending")
                OrderFilter.CANCELLED -> orderRepository.getUserOrders(search, "Cancelled")
                OrderFilter.CONFIRMED -> orderRepository.getUserOrders(search, "Confirmed")
                OrderFilter.PREPARING -> orderRepository.getUserOrders(search, "Preparing")
                OrderFilter.READY -> orderRepository.getUserOrders(search, "Ready for Pickup")
                else -> orderRepository.getUserOrders(search)
            }
        }.flattenMerge()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        getUserOrders()
    }

    fun onAction(action: OrdersAction){
        when(action){
            OrdersAction.GetUserOrders -> getUserOrders()
            is OrdersAction.UpdateFilter -> updateFilter(action.filter)
            is OrdersAction.UpdateSearch -> onSearchValueChange(action.search)
        }
    }

    fun onSearchValueChange(value: String){
        _searchText.update { value }
    }

    fun updateFilter(option: OrderFilter) {
        _filterOption.value = option
    }

    fun getUserOrders(){
        Timber.d("getUserOrders")
        viewModelScope.launch{
            setLoadingState(true)
            orderRepository.getUserOrders()
                .onError { result ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = result
                        )
                    )
                    setLoadingState(false)
                }
            setLoadingState(false)
        }
    }
}