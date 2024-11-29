package com.example.mobile.orders.data.remote

import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.domain.repository.OrderRepository
import com.example.mobile.orders.domain.service.OrderService
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.menu.data.remote.dto.MenuDto
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.toOrderEntity
import com.example.mobile.orders.presentation.create_order.OrderForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.first

class OrderRepositoryImpl @Inject constructor(
    private val orderService: OrderService,
    private val networkConnectivityObserver: ConnectivityObserver,
    private val orderDao: OrderDao
    ) : OrderRepository{

    private val isOnlineFlow: StateFlow<Boolean> = networkConnectivityObserver.observe()
        .distinctUntilChanged()
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private suspend fun isOnline(): Boolean {
        return isOnlineFlow.first()
    }

    fun syncOrdersWithDb(){

    }

    override suspend fun getUserOrderItems(): NetworkResult<List<OrderMenuItemDto>> {
        Timber.d("Order getUserOrders")
        return safeCall<OrderMenuItemDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.getUserOrderItems()
            }
        )
    }

    override suspend fun getUserOrders(): NetworkResult<List<OrderDto>> {
        Timber.d("Order getUserOrders")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.getUserOrders()
            }
        )
    }

    override suspend fun makeUserPickUpOrder(orderForm: OrderForm): NetworkResult<List<OrderDto>> {
        Timber.d("Order makeUserPickUpOrder")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.makeUserPickUpOrder(orderForm)
            }
        )
    }

}