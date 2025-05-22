package com.yehorsk.platea.cart.domain.models

import com.yehorsk.platea.cart.data.db.model.PivotCartItemEntity

data class CartItem(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val menuId: String,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    val availability: Boolean,
    val isFavorite: Boolean,
    val pivot: PivotCartItemEntity
)

data class PivotCartItem(
    val id: Int,
    val userId: Int,
    val menuItemId: Int,
    val quantity: Int,
    val price: Double,
    val createdAt: String,
    val updatedAt: String
)
