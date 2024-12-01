package com.example.mobile.orders.presentation.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val orderRepositoryImpl: OrderRepositoryImpl,
    val orderDao: OrderDao
): ViewModel(){

    val isNetwork = networkConnectivityObserver.observe()

    val orderItemsUiState: StateFlow<List<OrderEntity>> = orderDao.getUserOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getUserOrders()
    }

    fun getUserOrders(){
        Timber.d("getUserOrders")
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if(available){
                    val result = orderRepositoryImpl.getUserOrders()
                    when(result){
                        is NetworkResult.Error -> {
                            if(result.code == 503){
                                _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                            }else{
                                _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                            }
                        }
                        is NetworkResult.Success -> {
                            Timber.d("Result: ${result.data}")
                            _sideEffectChannel.send(SideEffect.ShowToast("Your orders are here"))
                        }
                    }
                }else{
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
            }
        }
    }
}