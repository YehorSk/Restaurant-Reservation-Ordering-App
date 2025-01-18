package com.example.mobile.orders.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "order_item_table",
    primaryKeys = ["pivot_id"]
)
data class OrderItemEntity(
    val id: Int,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
    @ColumnInfo(name = "menu_id") val menuId: Int,
    val name: String,
    @ColumnInfo(name = "short_description") val shortDescription: String,
    @ColumnInfo(name = "long_description") val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    @Embedded val pivot: PivotOrderItemEntity
)

data class PivotOrderItemEntity(
    @ColumnInfo(name = "pivot_id") val id: Int,
    @ColumnInfo(name = "order_id") val orderId: Int,
    @ColumnInfo(name = "menu_item_id") val menuItemId: Int,
    val quantity: Int,
    @ColumnInfo(name = "pivot_price") val price: Double,
    @ColumnInfo(name = "pivot_created_at") val createdAt: String,
    @ColumnInfo(name = "pivot_updated_at") val updatedAt: String
)

//fun OrderItemEntity.toOrderItem() = OrderMenuItemDto(
//    id = this.id,
//    createdAt = this.createdAt,
//    updatedAt = this.updatedAt,
//    menuId = this.menuId,
//    name = this.name,
//    shortDescription = this.shortDescription,
//    longDescription = this.longDescription,
//    recipe = this.recipe,
//    picture = this.picture,
//    price = this.price
//)