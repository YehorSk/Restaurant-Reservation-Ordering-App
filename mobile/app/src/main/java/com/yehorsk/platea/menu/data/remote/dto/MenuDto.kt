package com.yehorsk.platea.menu.data.remote.dto

import com.yehorsk.platea.core.data.remote.BooleanIntSerializer
import com.yehorsk.platea.menu.data.db.model.MenuEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val name: String,
    val description: String,
    @Serializable(with = BooleanIntSerializer::class)
    val availability: Boolean,
    val items: List<MenuItemDto>
)

fun MenuDto.toMenuEntity(): MenuEntity {
    return MenuEntity(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        description = this.description,
        availability = this.availability
    )
}