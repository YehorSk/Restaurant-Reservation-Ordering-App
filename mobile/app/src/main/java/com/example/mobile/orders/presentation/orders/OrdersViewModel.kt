package com.example.mobile.orders.presentation.orders

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.remote.OrderFilter
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
        networkConnectivityObserver: ConnectivityObserver,
        orderRepositoryImpl: OrderRepositoryImpl,
        orderDao: OrderDao,
        preferencesRepository: MainPreferencesRepository,
    ): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao, preferencesRepository){

    private val _filterOption = MutableStateFlow(OrderFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    val orderItemsUiState: StateFlow<List<OrderEntity>> = orderDao.getUserOrders()
        .combine(_filterOption) { orders, filter ->
            when (filter) {
                OrderFilter.COMPLETED -> orders.filter { it.status == "Completed" }
                OrderFilter.PENDING -> orders.filter { it.status == "Pending" }
                OrderFilter.CANCELLED -> orders.filter { it.status == "Cancelled" }
                OrderFilter.CONFIRMED -> orders.filter { it.status == "Confirmed" }
                OrderFilter.PREPARING -> orders.filter { it.status == "Preparing" }
                OrderFilter.READY -> orders.filter { it.status == "Ready for Pickup" }
                else -> orders
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        getUserOrders()
    }

    fun updateFilter(option: OrderFilter) {
        _filterOption.value = option
    }

    fun getUserOrders(){
        Timber.d("getUserOrders")
        viewModelScope.launch{
            setLoadingState(true)
            orderRepositoryImpl.getUserOrders()
                .onSuccess { data, message ->

                }
                .onError { result ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(result))
                    setLoadingState(false)
                }
            setLoadingState(false)
        }
    }
}