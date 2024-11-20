package com.example.mobile.orders.presentation.create_order

import androidx.lifecycle.ViewModel
import com.example.mobile.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
): ViewModel(){

    val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState = _uiState.asStateFlow()

    val isNetwork = networkConnectivityObserver.observe()

    fun updateOrderType(type: Int){
        _uiState.update {
            it.copy(orderType = type)
        }
    }

    fun updateRequest(request: String){
        _uiState.update {
            it.copy(request = request)
        }
    }


}