package com.example.mobile.menu.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobile.menu.data.remote.dto.MenuItem

@Entity("menu_item_table")
data class MenuItemEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    @ColumnInfo("menu_id")
    val menuId: String,
    val name: String,
    @ColumnInfo("short_description")
    val shortDescription: String,
    @ColumnInfo("long_description")
    val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: String,
    @ColumnInfo("is_favorite")
    val isFavorite: Boolean = false
)

fun MenuItemEntity.toMenuItem(): MenuItem {
    return MenuItem(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        menuId = this.menuId,
        name = this.name,
        shortDescription = this.shortDescription,
        longDescription = this.longDescription,
        recipe = this.recipe,
        picture = this.picture,
        price = this.price
    )
}