package com.yehorsk.platea.orders.data.mappers

import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.models.toOrderMenuItem

fun OrderEntity.toOrder(): Order{
    return Order(
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
        code = this.code,
        address = this.address,
        instructions = this.instructions,
        startTime = this.startTime,
        endTime = this.endTime,
        completedAt = this.completedAt,
        phone = this.phone,
        date = this.date
    )
}

fun OrderWithOrderItems.toOrder() = Order(
    id = order.id,
    code = order.code,
    createdAt = order.createdAt,
    updatedAt = order.updatedAt,
    clientId = order.clientId,
    tableId = order.tableId,
    waiterId = order.waiterId,
    reservationId = order.reservationId,
    price = order.price,
    status = order.status,
    phone = order.phone,
    specialRequest = order.specialRequest,
    address = order.address,
    instructions =order.instructions,
    orderType = order.orderType,
    startTime = order.startTime,
    endTime = order.endTime,
    date = order.date,
    completedAt = order.completedAt,
    orderItems = orderItems.map { it.toOrderMenuItem() }
)