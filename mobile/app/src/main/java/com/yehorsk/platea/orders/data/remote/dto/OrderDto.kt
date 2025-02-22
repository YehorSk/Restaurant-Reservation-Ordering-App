package com.yehorsk.platea.orders.data.remote.dto

import com.yehorsk.platea.orders.data.db.model.OrderEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String,
    val code: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("client_id")
    val clientId: Int? = null,
    @SerialName("table_id")
    val tableId: Int? = null,
    @SerialName("waiter_id")
    val waiterId: Int? = null,
    @SerialName("reservation_id")
    val reservationId: Int? = null,
    val price: Double,
    val status: String,
    val phone: String? = null,
    @SerialName("special_request")
    val specialRequest: String? = null,
    val address: String? = null,
    val instructions: String? = null,
    @SerialName("order_type")
    val orderType: Int,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("completed_at")
    val completedAt: String? = null,
    @SerialName("order_items")
    val orderItems: List<OrderMenuItemDto>
)

fun OrderDto.toOrderEntity(): OrderEntity{
    return OrderEntity(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        clientId = this.clientId,
        tableId = this.tableId,
        waiterId = this.waiterId,
        reservationId = this.reservationId,
        price = this.price,
        status = this.status,
        specialRequest = this.specialRequest,
        address = this.address,
        orderType = this.orderType,
        instructions = this.instructions,
        code = this.code,
        startTime = this.startTime,
        endTime = this.endTime,
        completedAt = this.completedAt,
        phone = this.phone
    )
}