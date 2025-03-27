package com.yehorsk.platea.menu.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import com.yehorsk.platea.menu.data.remote.dto.MenuItemDto

@Entity("menu_table")
data class MenuEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    val name: String,
    val description: String,
    val availability: Boolean
)

fun MenuEntity.toMenu(items: List<MenuItemDto>): MenuDto {
    return MenuDto(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        description = this.description,
        availability = this.availability,
        items = items
    )
}