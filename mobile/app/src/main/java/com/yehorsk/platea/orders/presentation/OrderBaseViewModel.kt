package com.yehorsk.platea.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import com.yehorsk.platea.orders.data.remote.OrderRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class OrderBaseViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val orderRepositoryImpl: OrderRepositoryImpl,
    val orderDao: OrderDao,
    val preferencesRepository: MainPreferencesRepository
): ViewModel(){

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userAddress: StateFlow<String?> = preferencesRepository.userAddressFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    protected val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    val ordersUiState: StateFlow<List<OrderWithOrderItems>> = orderDao.getOrderWithOrderItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val isNetwork = MutableStateFlow<Boolean>(networkConnectivityObserver.isAvailable)

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init{
        viewModelScope.launch{
            networkConnectivityObserver.observe().collect { status ->
                isNetwork.value = status
            }
        }
        viewModelScope.launch {
            userAddress.collect { address ->
                _uiState.update { it.copy(orderForm = it.orderForm.copy(address = address ?: "")) }
            }
        }
    }

    protected fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    protected fun setPlacesLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoadingPlaces = isLoading) }
    }

}