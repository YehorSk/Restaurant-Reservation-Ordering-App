package com.example.mobile.orders.presentation.orders

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
        networkConnectivityObserver: ConnectivityObserver,
        orderRepositoryImpl: OrderRepositoryImpl,
        orderDao: OrderDao
    ): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao){

    val orderItemsUiState: StateFlow<List<OrderEntity>> = orderDao.getUserOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        getUserOrders()
    }

    fun getUserOrders(){
        Timber.d("getUserOrders")
        viewModelScope.launch{
            setLoadingState(true)
            orderRepositoryImpl.getUserOrders()
                .onError { result ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(result))
                    setLoadingState(false)
                }
            setLoadingState(false)
        }
    }
}