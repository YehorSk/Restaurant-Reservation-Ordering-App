package com.yehorsk.platea.orders.data.remote

import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.db.model.OrderItemEntity
import com.yehorsk.platea.orders.data.remote.dto.OrderDto
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.data.remote.dto.toOrderEntity
import com.yehorsk.platea.orders.data.remote.dto.toOrderMenuItemEntity
import com.yehorsk.platea.orders.data.remote.service.OrderService
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.orders.presentation.order_details.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.first

class OrderRepositoryImpl @Inject constructor(
    private val orderService: OrderService,
    private val orderDao: OrderDao,
    private val cartDao: CartDao,
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

    override suspend fun getTables(): Result<List<TableDto>, AppError> {
        Timber.d("Order getTables")
        return safeCall<TableDto>(
            execute = {
                orderService.getTables()
            }
        )
    }

    override suspend fun updateOrderStatus(id: String, status: Status): Result<List<OrderDto>, AppError> {
        Timber.d("Order markOrderAsCompleted $id")
        return safeCall<OrderDto>(
            execute = {
                orderService.updateOrderStatus(id, status = status)
            },
            onSuccess = { data ->
                orderDao.insertOrder(data.first().toOrderEntity())
                orderDao.insertOrderItems(data.first().orderItems.map {
                    it.toOrderMenuItemEntity()
                })
            }
        )
    }

}