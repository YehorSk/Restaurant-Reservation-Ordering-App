package com.example.mobile.orders.domain.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto

interface OrderRepository {

    suspend fun getUserOrderItems(): NetworkResult<List<OrderMenuItemDto>>

}