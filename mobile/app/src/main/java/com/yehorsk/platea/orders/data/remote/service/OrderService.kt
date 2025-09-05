package com.yehorsk.platea.orders.data.remote.service

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.orders.data.remote.dto.OrderDto
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import com.yehorsk.platea.orders.presentation.order_details.Status
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("order/waiter/order")
    suspend fun makeWaiterOrder(@Body orderForm: OrderForm) : ResponseDto<OrderDto>

    @POST("order/user/pickup")
    suspend fun makeUserPickUpOrder(@Body orderForm: OrderForm) : ResponseDto<OrderDto>

    @POST("order/user/delivery")
    suspend fun makeUserDeliveryOrder(@Body orderForm: OrderForm) : ResponseDto<OrderDto>

    @GET("table/waiter/tables")
    suspend fun getTables() : ResponseDto<TableDto>

    @PUT("order/admin/updateOrder/{id}")
    suspend fun updateOrderStatus(@Path("id") id: String, @Body status: Status) : ResponseDto<OrderDto>

}