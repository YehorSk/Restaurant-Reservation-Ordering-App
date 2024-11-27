package com.example.mobile.orders.data.remote.dto

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