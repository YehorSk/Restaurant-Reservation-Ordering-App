package com.yehorsk.platea.orders.domain.repository

import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.remote.dto.OrderDto
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.models.OrderMenuItem
import com.yehorsk.platea.orders.domain.models.Table
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.orders.presentation.order_details.Status
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun getUserOrderItems(): Result<List<OrderMenuItem>, AppError>

    suspend fun getUserOrders() : Result<List<OrderDto>, AppError>

    suspend fun getUserOrderDetails(id: String) : Result<List<OrderDto>, AppError>

    suspend fun cancelUserOrder(id: String) : Result<List<OrderDto>, AppError>

    suspend fun makeUserPickUpOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun makeUserDeliveryOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun makeWaiterOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun getTables() : Result<List<TableDto>, AppError>

    suspend fun updateOrderStatus(id: String, status: Status) : Result<List<OrderDto>, AppError>

    fun getOrderWithOrderItems(): Flow<List<Order>>

    fun getUserOrders(search: String = "", filter: String = ""): Flow<List<Order>>

    fun getAllCartItems() : Flow<List<CartItem>>

}