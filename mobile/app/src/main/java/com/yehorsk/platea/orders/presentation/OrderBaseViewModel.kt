package com.yehorsk.platea.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.repository.OrderRepository
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
    val orderRepository: OrderRepository,
    val preferencesRepository: MainPreferencesRepository,
    val restaurantInfoDao: RestaurantInfoDao
): ViewModel(){

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userAddress: StateFlow<String?> = preferencesRepository.userAddressFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userPhone: StateFlow<String?> = preferencesRepository.userPhoneFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userCountryCode: StateFlow<String?> = preferencesRepository.userCountryCodeFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    protected val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    val ordersUiState: StateFlow<List<Order>> = orderRepository.getOrderWithOrderItems()
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
        viewModelScope.launch {
            userPhone.collect { phone ->
                _uiState.update { state ->
                    val updatedPhone = phone ?: ""
                    val updatedFullPhone = "${state.countryCode}${updatedPhone}"
                    state.copy(
                        phone = updatedPhone,
                        orderForm = state.orderForm.copy(fullPhone = updatedFullPhone)
                    )
                }
            }
        }
        viewModelScope.launch {
            userCountryCode.collect { code ->
                _uiState.update { state ->
                    val updatedCountryCode = code ?: ""
                    val updatedFullPhone = "${updatedCountryCode}${state.phone}"
                    state.copy(orderForm = state.orderForm.copy(fullPhone = updatedFullPhone))
                    state.copy(countryCode = updatedCountryCode)
                }
            }
        }
    }

    protected fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

}