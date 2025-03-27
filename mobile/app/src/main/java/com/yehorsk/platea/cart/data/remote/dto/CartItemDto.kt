package com.yehorsk.platea.cart.data.remote.dto


import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.cart.data.db.model.PivotCartItemEntity
import com.yehorsk.platea.core.data.remote.BooleanIntSerializer
import com.yehorsk.platea.menu.data.remote.dto.MenuItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemDto(
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
    @Serializable(with = BooleanIntSerializer::class)
    val availability: Boolean,
    val pivot: Pivot,
    val isFavorite: Boolean
)

@Serializable
data class Pivot(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("menu_item_id")
    val menuItemId: Int,
    val quantity: Int,
    val price: Double,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)

fun CartItemDto.toMenuItem() = MenuItemDto(
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
    isFavorite = this.isFavorite,
    availability = this.availability
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
    isFavorite = isFavorite,
    availability = availability
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