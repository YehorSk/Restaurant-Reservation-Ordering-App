package com.example.mobile.orders.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import kotlinx.serialization.SerialName

@Entity("order_table")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val code: String,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    @ColumnInfo("client_id")
    val clientId: Int? = null,
    @ColumnInfo("table_id")
    val tableId: Int? = null,
    @ColumnInfo("waiter_id")
    val waiterId: Int? = null,
    @ColumnInfo("reservation_id")
    val reservationId: Int? = null,
    val price: Double,
    val status: String,
    @ColumnInfo("special_request")
    val specialRequest: String? = null,
    val address: String? = null,
    val instructions: String? = null,
    @ColumnInfo("order_type")
    val orderType: Int,
    @ColumnInfo("start_time")
    val startTime: String,
    @ColumnInfo("end_time")
    val endTime: String,
    @ColumnInfo("completed_at")
    val completedAt: String? = null,
)

fun OrderEntity.toOrderDto(items: List<OrderMenuItemDto>): OrderDto{
    return OrderDto(
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
        orderType = this.orderType,
        orderItems = items,
        code = this.code,
        address = this.address,
        instructions = this.instructions,
        startTime = this.startTime,
        endTime = this.endTime,
        completedAt = this.completedAt
    )
}