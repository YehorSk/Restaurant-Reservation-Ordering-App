package com.example.mobile.orders.data.remote

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.domain.repository.OrderRepository
import com.example.mobile.orders.domain.service.OrderService
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.db.model.OrderItemEntity
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.toOrderEntity
import com.example.mobile.orders.data.remote.dto.toOrderMenuItemEntity
import com.example.mobile.orders.presentation.create_order.OrderForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
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

    suspend fun syncOrdersWithDb(items: List<OrderDto>) = withContext(Dispatchers.IO){
        val localOrders = orderDao.getOrderWithOrderItemsOnce()

        val serverOrderIds = items.map { it.id }.toSet()
        val serverOrderItemsIds = items.flatMap { order -> order.orderItems.map { it.id } }.toSet()

        val ordersToDelete = localOrders.filter { it.order.id !in serverOrderIds }
        val orderItemsToDelete = localOrders.flatMap { localOrder ->
            localOrder.orderItems.filter { it.id !in serverOrderItemsIds }
        }

        orderDao.runInTransaction {
            ordersToDelete.forEach{ order ->
                orderDao.deleteOrder(order.order)
            }
            deleteOrderItems(orderItemsToDelete)
            insert(items)
        }
    }

    suspend fun insert(orders: List<OrderDto>) = withContext(Dispatchers.IO) {
        for(item in orders){
            orderDao.insertOrder(item.toOrderEntity())
            for(orderItem in item.orderItems){
                orderDao.insertOrderItem(orderItem.toOrderMenuItemEntity())
            }
        }
    }

    suspend fun deleteOrderItems(orderItems: List<OrderItemEntity>) = withContext(Dispatchers.IO){
        for(item in orderItems){
            orderDao.deleteOrderItem(item)
        }
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
            },
            onSuccess = { data ->
                syncOrdersWithDb(data)
            }
        )
    }

    override suspend fun getUserOrderDetails(id: String): NetworkResult<List<OrderDto>> {
        Timber.d("Order getUserOrderDetails $id")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.getUserOrderDetails(id)
            },
            onSuccess = { data ->
                orderDao.insertOrder(data.first().toOrderEntity())
                orderDao.insertOrderItems(data.first().orderItems.map {
                    it.toOrderMenuItemEntity()
                })
            }
        )
    }

    override suspend fun cancelUserOrder(id: String): NetworkResult<List<OrderDto>> {
        Timber.d("Order cancelUserOrder $id")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.cancelUserOrder(id)
            },
            onSuccess = { data ->
                orderDao.insertOrder(data.first().toOrderEntity())
                orderDao.insertOrderItems(data.first().orderItems.map {
                    it.toOrderMenuItemEntity()
                })
            }
        )
    }

    override suspend fun repeatUserOrder(id: String): NetworkResult<List<OrderDto>> {
        Timber.d("Order repeatUserOrder $id")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.repeatUserOrder(id)
            },
            onSuccess = { data ->
//                orderDao.insertOrder(data.first().toOrderEntity())
//                orderDao.insertOrderItems(data.first().orderItems.map {
//                    it.toOrderMenuItemEntity()
//                })
            }
        )
    }


    override suspend fun makeUserPickUpOrder(orderForm: OrderForm): NetworkResult<List<OrderDto>> {
        Timber.d("Order makeUserPickUpOrder")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.makeUserPickUpOrder(orderForm)
            },
            onSuccess = {
                orderDao.deleteAllCartItems()
            }
        )
    }

    override suspend fun makeUserDeliveryOrder(orderForm: OrderForm): NetworkResult<List<OrderDto>> {
        Timber.d("Order makeUserDeliveryOrder")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.makeUserDeliveryOrder(orderForm)
            },
            onSuccess = {
                orderDao.deleteAllCartItems()
            }
        )
    }

    override suspend fun makeWaiterOrder(orderForm: OrderForm): NetworkResult<List<OrderDto>> {
        Timber.d("Order makeWaiterOrder")
        return safeCall<OrderDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                orderService.makeWaiterOrder(orderForm)
            },
            onSuccess = {
                orderDao.deleteAllCartItems()
            }
        )
    }


}