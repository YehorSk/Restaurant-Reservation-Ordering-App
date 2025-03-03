package com.yehorsk.platea.orders.presentation.orders

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.orders.data.remote.OrderRepositoryImpl
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
        orderRepositoryImpl: OrderRepositoryImpl,
        orderDao: OrderDao,
        preferencesRepository: MainPreferencesRepository,
    ): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao, preferencesRepository){

    private val _filterOption = MutableStateFlow(OrderFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val orderItemsUiState: StateFlow<List<OrderEntity>> = combine(_filterOption, _searchText) { filter, search ->
            when (filter) {
                OrderFilter.COMPLETED -> orderDao.getUserOrders(search, "Completed")
                OrderFilter.PENDING -> orderDao.getUserOrders(search, "Pending")
                OrderFilter.CANCELLED -> orderDao.getUserOrders(search, "Cancelled")
                OrderFilter.CONFIRMED -> orderDao.getUserOrders(search, "Confirmed")
                OrderFilter.PREPARING -> orderDao.getUserOrders(search, "Preparing")
                OrderFilter.READY -> orderDao.getUserOrders(search, "Ready for Pickup")
                else -> orderDao.getUserOrders(search)
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
            orderRepositoryImpl.getUserOrders()
                .onSuccess { data, message ->

                }
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