package com.yehorsk.platea.orders.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithOrderItems(
    @Embedded
    val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val orderItems: List<OrderItemEntity>
)
