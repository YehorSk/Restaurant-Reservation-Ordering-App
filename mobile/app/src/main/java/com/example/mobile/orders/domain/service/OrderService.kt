package com.example.mobile.orders.domain.service

import com.example.mobile.core.data.remote.dto.ResponseDto
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.ReservationDto
import com.example.mobile.orders.data.remote.dto.TableDto
import com.example.mobile.orders.data.remote.dto.TimeSlotDto
import com.example.mobile.orders.presentation.OrderForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService {

    @GET("order/user/cartItems")
    suspend fun getUserOrderItems() : ResponseDto<OrderMenuItemDto>

    @GET("order/user/orders")
    suspend fun getUserOrders() : ResponseDto<OrderDto>

    @GET("order/user/orders/{id}")
    suspend fun getUserOrderDetails(@Path("id") id: String) : ResponseDto<OrderDto>

    @GET("order/user/orders/cancel/{id}")
    suspend fun cancelUserOrder(@Path("id") id: String) : ResponseDto<OrderDto>

    @GET("order/user/orders/repeat/{id}")
    suspend fun repeatUserOrder(@Path("id") id: String) : ResponseDto<OrderDto>

    @POST("order/waiter/order")
    suspend fun makeWaiterOrder(@Body orderForm: OrderForm) : ResponseDto<OrderDto>

    @POST("order/user/pickup")
    suspend fun makeUserPickUpOrder(@Body orderForm: OrderForm) : ResponseDto<OrderDto>

    @POST("order/user/delivery")
    suspend fun makeUserDeliveryOrder(@Body orderForm: OrderForm) : ResponseDto<OrderDto>

    @GET("table/waiter/tables")
    suspend fun getTables() : ResponseDto<TableDto>

    @POST("reservation/user/getTimeSlots")
    suspend fun getAvailableTimeSlots(@Body orderForm: OrderForm) : ResponseDto<TimeSlotDto>

    @POST("reservation/user/createReservation")
    suspend fun createReservation(@Body orderForm: OrderForm) : ResponseDto<ReservationDto>

    @GET("reservation/user/getReservations")
    suspend fun getReservations() : ResponseDto<ReservationDto>

}