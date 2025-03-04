package com.yehorsk.platea.core.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity("restaurant_info")
data class RestaurantInfoEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String? = null,
    @SerialName("opening_hours")
    val openingHours: String
)