package com.example.mobile.orders.domain.service

import com.example.mobile.core.data.remote.dto.ResponseDto
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import retrofit2.http.GET

interface OrderService {

    @GET("user-order-items")
    suspend fun getUserOrderItems() : ResponseDto<OrderMenuItemDto>

}