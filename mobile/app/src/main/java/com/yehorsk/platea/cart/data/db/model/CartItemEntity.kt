package com.yehorsk.platea.cart.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.yehorsk.platea.menu.data.remote.dto.MenuItemDto

@Entity(
    tableName = "cart_table",
    primaryKeys = ["pivot_id"]
)
data class CartItemEntity(
    val id: Int,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
    @ColumnInfo(name = "menu_id") val menuId: String,
    val name: String,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "long_description") val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    val availability: Boolean,
    val isFavorite: Boolean,
    @Embedded val pivot: PivotCartItemEntity
)

data class PivotCartItemEntity(
    @ColumnInfo(name = "pivot_id") val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "menu_item_id") val menuItemId: Int,
    val quantity: Int,
    @ColumnInfo(name = "pivot_price") val price: Double,
    @ColumnInfo(name = "pivot_created_at") val createdAt: String,
    @ColumnInfo(name = "pivot_updated_at") val updatedAt: String
)

fun CartItemEntity.toMenuItem() = MenuItemDto(
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
