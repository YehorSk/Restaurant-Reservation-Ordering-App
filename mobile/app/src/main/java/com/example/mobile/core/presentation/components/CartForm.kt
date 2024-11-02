package com.example.mobile.core.presentation.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartForm(
    @SerialName("pivot_id")
    val pivotId: Int = 0,
    val quantity: Int = 1,
    val price: Double = 0.00,
    @SerialName("menu_item_id")
    val menuItemId: Int = 0,
    val isFavorite: Boolean = false
)