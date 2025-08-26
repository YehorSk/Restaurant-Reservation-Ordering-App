package com.yehorsk.platea.orders.data.remote

import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.cart.data.mappers.toCartItem
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.map
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.orders.data.db.model.OrderItemEntity
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import com.yehorsk.platea.orders.data.mappers.toOrder
import com.yehorsk.platea.orders.data.mappers.toTable
import com.yehorsk.platea.orders.data.remote.dto.OrderDto
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.data.remote.dto.toOrderEntity
import com.yehorsk.platea.orders.data.remote.dto.toOrderMenuItemEntity
import com.yehorsk.platea.orders.data.remote.service.OrderService
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.models.OrderMenuItem
import com.yehorsk.platea.orders.domain.models.Table
import com.yehorsk.platea.orders.domain.models.toOrderMenuItem
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.orders.presentation.order_details.Status
import com.yehorsk.platea.reservations.data.remote.dto.toReservationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun getUserOrderItems(): Result<List<OrderMenuItem>, AppError> {
        Timber.d("Order getUserOrders")
        return safeCall<OrderMenuItemDto>(
            execute = {
                orderService.getUserOrderItems()
            }
        ).map { data ->
            data.map {
                it.toOrderMenuItem()
            }
        }
    }

    override suspend fun getUserOrders(): Result<List<OrderDto>, AppError> {
        Timber.d("Order getUserOrders")
        return safeCall<OrderDto>(
            execute = {
                orderService.getUserOrders()
            }
        ).onSuccess { data, _ ->
            syncOrdersWithDb(data)
        }
    }

    override suspend fun getUserOrderDetails(id: String): Result<List<OrderDto>, AppError> {
        Timber.d("Order getUserOrderDetails $id")
        return safeCall<OrderDto>(
            execute = {
                orderService.getUserOrderDetails(id)
            }
        ).onSuccess { data, _ ->
            orderDao.insertOrder(data.first().toOrderEntity())
            orderDao.insertOrderItems(data.first().orderItems.map {
                it.toOrderMenuItemEntity()
            })
        }
    }

    override suspend fun cancelUserOrder(id: String): Result<List<OrderDto>, AppError> {
        Timber.d("Order cancelUserOrder $id")
        return safeCall<OrderDto>(
            execute = {
                orderService.cancelUserOrder(id)
            }
        ).onSuccess { data, _ ->
            orderDao.insertOrder(data.first().toOrderEntity())
            orderDao.insertOrderItems(data.first().orderItems.map {
                it.toOrderMenuItemEntity()
            })
        }
    }


    override suspend fun makeUserPickUpOrder(orderForm: OrderForm): Result<List<OrderDto>, AppError> {
        Timber.d("Order makeUserPickUpOrder")
        return safeCall<OrderDto>(
            execute = {
                orderService.makeUserPickUpOrder(orderForm)
            }
        ).onSuccess { data, _ ->
            orderDao.deleteAllCartItems()
        }
    }

    override suspend fun makeUserDeliveryOrder(orderForm: OrderForm): Result<List<OrderDto>, AppError> {
        Timber.d("Order makeUserDeliveryOrder")
        return safeCall<OrderDto>(
            execute = {
                orderService.makeUserDeliveryOrder(orderForm)
            }
        ).onSuccess { data, _ ->
            orderDao.deleteAllCartItems()
        }
    }

    override suspend fun makeWaiterOrder(orderForm: OrderForm): Result<List<OrderDto>, AppError> {
        Timber.d("Order makeWaiterOrder")
        return safeCall<OrderDto>(
            execute = {
                orderService.makeWaiterOrder(orderForm)
            }
        ).onSuccess { data, _ ->
            orderDao.deleteAllCartItems()
        }
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
            }
        ).onSuccess { data, _ ->
            orderDao.insertOrder(data.first().toOrderEntity())
            orderDao.insertOrderItems(data.first().orderItems.map {
                it.toOrderMenuItemEntity()
            })
        }
    }

    override fun getOrderWithOrderItems(): Flow<List<Order>> {
        return orderDao
            .getOrderWithOrderItems()
            .map { data ->
                data.map {
                    it.toOrder()
                }
            }
    }


    override fun getUserOrders(
        search: String,
        filter: String
    ): Flow<List<Order>> {
        return orderDao
            .getUserOrders(search, filter)
            .map { data ->
                data.map {
                    it.toOrder()
                }
            }
    }

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllItems().map { data ->
            data.map { it.toCartItem() }
        }
    }

}