package com.example.mobile.orders.data.remote.dto

import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.db.model.PivotCartItemEntity
import com.example.mobile.orders.data.db.model.OrderItemEntity
import com.example.mobile.orders.data.db.model.PivotOrderItemEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderMenuItemDto(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("menu_id")
    val menuId: Int,
    val name: String,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("long_description")
    val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    val pivot: Pivot
)

@Serializable
data class Pivot(
    val id: Int,
    @SerialName("order_id")
    val orderId: Int?=null,
    @SerialName("user_id")
    val userId: Int?=null,
    @SerialName("menu_item_id")
    val menuItemId: Int,
    val quantity: Int,
    val price: Double,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)

fun OrderMenuItemDto.toOrderMenuItemEntity(): OrderItemEntity{
    return OrderItemEntity(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        menuId = this.menuId,
        name = this.name,
        shortDescription = this.shortDescription,
        longDescription = this.longDescription,
        recipe = this.recipe,
        picture = this.picture,
        price = this.price,
        pivot = this.pivot.toPivotEntity()
    )
}

fun Pivot.toPivotEntity() = PivotOrderItemEntity(
    id = this.id,
    orderId = this.orderId!!.toInt(),
    menuItemId = this.menuItemId,
    quantity = this.quantity,
    price = this.price,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun OrderMenuItemDto.toCartItemEntity() = CartItemEntity(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    menuId = this.menuId,
    name = this.name,
    shortDescription = this.shortDescription,
    longDescription = this.longDescription,
    recipe = this.recipe,
    picture = this.picture,
    price = this.price,
    pivot = this.pivot.toPivotCartItemEntity(),
    isFavorite = false
)

fun Pivot.toPivotCartItemEntity() = PivotCartItemEntity(
    id = this.id,
    userId = this.userId!!.toInt(),
    menuItemId = this.menuItemId,
    quantity = this.quantity,
    price = this.price,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)