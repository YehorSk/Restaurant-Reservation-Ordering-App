package com.example.mobile.orders.presentation

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.orders.presentation.create_order.CreateOrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
open class OrderBaseViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val orderRepositoryImpl: OrderRepositoryImpl,
    val orderDao: OrderDao
): ViewModel(){

    protected val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState = _uiState.asStateFlow()

    val ordersUiState: StateFlow<List<OrderWithOrderItems>> = orderDao.getOrderWithOrderItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val isNetwork = networkConnectivityObserver.observe()

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

}