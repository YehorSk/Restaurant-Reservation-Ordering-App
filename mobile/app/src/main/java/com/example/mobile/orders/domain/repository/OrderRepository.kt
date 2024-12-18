package com.example.mobile.orders.domain.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.AppError
import com.example.mobile.core.domain.Result
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.presentation.create_order.OrderForm

interface OrderRepository {

    suspend fun getUserOrderItems(): Result<List<OrderMenuItemDto>, AppError>

    suspend fun getUserOrders() : Result<List<OrderDto>, AppError>

    suspend fun getUserOrderDetails(id: String) : Result<List<OrderDto>, AppError>

    suspend fun cancelUserOrder(id: String) : Result<List<OrderDto>, AppError>

    suspend fun repeatUserOrder(id: String) : Result<List<OrderDto>, AppError>

    suspend fun makeUserPickUpOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun makeUserDeliveryOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

    suspend fun makeWaiterOrder(orderForm: OrderForm) : Result<List<OrderDto>, AppError>

}