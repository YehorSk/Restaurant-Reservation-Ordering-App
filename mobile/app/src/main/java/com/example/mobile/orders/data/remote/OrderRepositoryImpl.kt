package com.example.mobile.orders.data.remote

import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.domain.AppError
import com.example.mobile.core.domain.Result
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.domain.repository.OrderRepository
import com.example.mobile.orders.domain.service.OrderService
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.db.model.OrderItemEntity
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.toCartItemEntity
import com.example.mobile.orders.data.remote.dto.toOrderEntity
import com.example.mobile.orders.data.remote.dto.toOrderMenuItemEntity
import com.example.mobile.orders.presentation.create_order.OrderForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.first

class OrderRepositoryImpl @Inject constructor(
    private val orderService: OrderService,
    private val orderDao: OrderDao,
    private val cartDao: CartDao
    ) : OrderRepository{


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

    override suspend fun getUserOrderItems(): Result<List<OrderMenuItemDto>, AppError> {
        Timber.d("Order getUserOrders")
        return safeCall<OrderMenuItemDto>(
            execute = {
                orderService.getUserOrderItems()
            }
        )
    }

    override suspend fun getUserOrders(): Result<List<OrderDto>, AppError> {
        Timber.d("Order getUserOrders")
        return safeCall<OrderDto>(
            execute = {
                orderService.getUserOrders()
            },
            onSuccess = { data ->
                syncOrdersWithDb(data)
            }
        )
    }

    override suspend fun getUserOrderDetails(id: String): Result<List<OrderDto>, AppError> {
        Timber.d("Order getUserOrderDetails $id")
        return safeCall<OrderDto>(
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

    override suspend fun cancelUserOrder(id: String): Result<List<OrderDto>, AppError> {
        Timber.d("Order cancelUserOrder $id")
        return safeCall<OrderDto>(
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

    override suspend fun repeatUserOrder(id: String): Result<List<OrderDto>, AppError> {
        Timber.d("Order repeatUserOrder $id")
        return safeCall<OrderDto>(
            execute = {
                orderService.repeatUserOrder(id)
            },
            onSuccess = { data ->
                cartDao.insertItems(data.first().orderItems.map {
                    it.toCartItemEntity()
                })
            }
        )
    }


    override suspend fun makeUserPickUpOrder(orderForm: OrderForm): Result<List<OrderDto>, AppError> {
        Timber.d("Order makeUserPickUpOrder")
        return safeCall<OrderDto>(
            execute = {
                orderService.makeUserPickUpOrder(orderForm)
            },
            onSuccess = {
                orderDao.deleteAllCartItems()
            }
        )
    }

    override suspend fun makeUserDeliveryOrder(orderForm: OrderForm): Result<List<OrderDto>, AppError> {
        Timber.d("Order makeUserDeliveryOrder")
        return safeCall<OrderDto>(
            execute = {
                orderService.makeUserDeliveryOrder(orderForm)
            },
            onSuccess = {
                orderDao.deleteAllCartItems()
            }
        )
    }

    override suspend fun makeWaiterOrder(orderForm: OrderForm): Result<List<OrderDto>, AppError> {
        Timber.d("Order makeWaiterOrder")
        return safeCall<OrderDto>(
            execute = {
                orderService.makeWaiterOrder(orderForm)
            },
            onSuccess = {
                orderDao.deleteAllCartItems()
            }
        )
    }
}