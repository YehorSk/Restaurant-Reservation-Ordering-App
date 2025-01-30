package com.yehorsk.platea.orders.domain.repository

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.remote.dto.OrderDto
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.presentation.OrderForm

interface OrderRepository {

    suspend fun getUserOrderItems(): Result<List<OrderMenuItemDto>, AppError>

    suspend fun getUserOrders() : Result<List<OrderDto>, AppError>

    suspend fun getUserOrderDetails(id: String) : Result<List<OrderDto>, AppError>

    suspend fun cancelUserOrder(id: String) : Result<List<OrderDto>, AppError>

    suspend fun repeatUserOrder(id: String) : Result<List<OrderDto>, AppError>

    suspend fun makeUserPickUpOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun makeUserDeliveryOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun makeWaiterOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun getTables() : Result<List<TableDto>, AppError>

    suspend fun markOrderAsCancelled(id: String) : Result<List<OrderDto>, AppError>

    suspend fun markOrderAsCompleted(id: String) : Result<List<OrderDto>, AppError>

    suspend fun markOrderAsConfirmed(id: String) : Result<List<OrderDto>, AppError>

    suspend fun markOrderAsPreparing(id: String) : Result<List<OrderDto>, AppError>

    suspend fun markOrderAsReadyForPickup(id: String) : Result<List<OrderDto>, AppError>
}