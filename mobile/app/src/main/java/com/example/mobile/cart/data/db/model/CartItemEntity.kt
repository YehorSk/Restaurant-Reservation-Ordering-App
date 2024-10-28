package com.example.mobile.cart.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.example.mobile.menu.data.remote.dto.MenuItem

@Entity(
    tableName = "cart_table",
    primaryKeys = ["pivot_id"]
)
data class CartItemEntity(
    val id: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
    @ColumnInfo(name = "menu_id") val menuId: String,
    val name: String,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "long_description") val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    @ColumnInfo("is_favorite") val isFavorite: Boolean = false,
    @Embedded val pivot: PivotCartItemEntity
)

data class PivotCartItemEntity(
    @ColumnInfo(name = "pivot_id") val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "menu_item_id") val menuItemId: String,
    val quantity: Int,
    @ColumnInfo(name = "pivot_price") val price: Double,
    @ColumnInfo(name = "pivot_created_at") val createdAt: String,
    @ColumnInfo(name = "pivot_updated_at") val updatedAt: String
)

fun CartItemEntity.toMenuItem() = MenuItem(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    menuId = this.menuId,
    name = this.name,
    shortDescription = this.shortDescription,
    longDescription = this.longDescription,
    recipe = this.recipe,
    picture = this.picture,
    price = this.price.toString()
)