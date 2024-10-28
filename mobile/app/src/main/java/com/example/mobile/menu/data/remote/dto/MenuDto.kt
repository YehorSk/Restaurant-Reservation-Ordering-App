package com.example.mobile.menu.data.remote.dto

import com.example.mobile.menu.data.db.model.MenuEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val name: String,
    val description: String,
    val availability: String,
    val items: List<MenuItem>
)

fun Menu.toMenuEntity(): MenuEntity {
    return MenuEntity(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        description = this.description,
        availability = this.availability
    )
}