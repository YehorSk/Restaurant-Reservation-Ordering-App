package com.yehorsk.platea.orders.domain.models

import com.yehorsk.platea.orders.data.db.model.OrderItemEntity
import com.yehorsk.platea.orders.data.db.model.PivotOrderItemEntity
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDtoPivot

data class OrderMenuItem(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val menuId: String,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    val pivot: OrderMenuItemPivot
)

data class OrderMenuItemPivot(
    val id: Int,
    val orderId: String?=null,
    val userId: Int?=null,
    val menuItemId: Int,
    val quantity: Int,
    val price: Double,
    val createdAt: String,
    val updatedAt: String,
)

fun OrderItemEntity.toOrderMenuItem() = OrderMenuItem(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    menuId = menuId,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    recipe = recipe,
    picture = picture,
    price = price,
    pivot = pivot.toOrderMenuItemPivot()
)

fun OrderMenuItemDto.toOrderMenuItem() = OrderMenuItem(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    menuId = menuId,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    recipe = recipe,
    picture = picture,
    price = price,
    pivot = pivot.toOrderMenuItemPivot()
)

fun PivotOrderItemEntity.toOrderMenuItemPivot() = OrderMenuItemPivot(
    id = this.id,
    orderId = this.orderId,
    menuItemId = this.menuItemId,
    quantity = this.quantity,
    price = this.price,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun OrderMenuItemDtoPivot.toOrderMenuItemPivot() = OrderMenuItemPivot(
    id = this.id,
    orderId = this.orderId,
    menuItemId = this.menuItemId,
    quantity = this.quantity,
    price = this.price,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)