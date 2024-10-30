package com.example.mobile.cart.data.remote.dto


import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.db.model.PivotCartItemEntity
import com.example.mobile.menu.data.remote.dto.MenuItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
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
    val pivot: Pivot,
    val isFavorite: Boolean
)

@Serializable
data class Pivot(
    val id: String,
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

fun CartItemDto.toMenuItem() = MenuItem(
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
    isFavorite = this.isFavorite
)

fun CartItemDto.toCartItemEntity() = CartItemEntity(
    id = id,
    menuId = menuId,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    recipe = recipe,
    picture = picture,
    createdAt = createdAt,
    updatedAt = updatedAt,
    price = price.toDouble(),
    pivot = pivot.toPivotEntity(),
    isFavorite = isFavorite
)

fun Pivot.toPivotEntity() = PivotCartItemEntity(
    id = id,
    userId = userId,
    menuItemId = menuItemId,
    quantity = quantity.toInt(),
    price = price.toDouble(),
    createdAt = createdAt,
    updatedAt = updatedAt
)