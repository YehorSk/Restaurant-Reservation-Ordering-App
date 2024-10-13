package com.example.mobile.menu.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val price: String
)
