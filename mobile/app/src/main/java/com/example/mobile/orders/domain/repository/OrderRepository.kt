package com.example.mobile.orders.domain.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.presentation.create_order.OrderForm

interface OrderRepository {

    suspend fun getUserOrderItems(): NetworkResult<List<OrderMenuItemDto>>

    suspend fun getUserOrders() : NetworkResult<List<OrderDto>>

    suspend fun getUserOrderDetails(id: String) : NetworkResult<List<OrderDto>>

    suspend fun cancelUserOrder(id: String) : NetworkResult<List<OrderDto>>

    suspend fun repeatUserOrder(id: String) : NetworkResult<List<OrderDto>>

    suspend fun makeUserPickUpOrder(orderForm: OrderForm) : NetworkResult<List<OrderDto>>

    suspend fun makeUserDeliveryOrder(orderForm: OrderForm) : NetworkResult<List<OrderDto>>

    suspend fun makeWaiterOrder(orderForm: OrderForm) : NetworkResult<List<OrderDto>>

}