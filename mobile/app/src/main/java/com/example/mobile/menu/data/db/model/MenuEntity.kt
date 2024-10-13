package com.example.mobile.menu.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val availability: String
)
