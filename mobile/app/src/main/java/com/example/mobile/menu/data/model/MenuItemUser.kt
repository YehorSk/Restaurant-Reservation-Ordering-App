package com.example.mobile.menu.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItemUser(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("menu_id")
    val menuId: String,
    val name: String,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("long_description")
    val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: String,
    val pivot: Pivot
)

@Serializable
data class Pivot(
    @SerialName("user_id")
    val userId: String,
    @SerialName("menu_item_id")
    val menuItemId: String,
    val quantity: String,
    val price: String,
    val note: String = "",
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)