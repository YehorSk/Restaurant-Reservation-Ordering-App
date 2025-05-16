package com.yehorsk.platea.menu.data.remote.dto

import com.yehorsk.platea.core.data.remote.BooleanIntSerializer
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: Int,
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
    val price: Double,
    @Serializable(with = BooleanIntSerializer::class)
    val availability: Boolean,
    val isFavorite: Boolean
)

fun MenuItemDto.toMenuItemEntity(): MenuItemEntity {
    return MenuItemEntity(
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
}