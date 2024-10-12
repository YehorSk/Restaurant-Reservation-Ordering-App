package com.example.mobile.core.presentation.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartForm(
    @SerialName("pivot_id")
    val pivotId: String = "",
    val quantity: Int = 1,
    val price: Double = 0.00,
    val note: String = "",
    @SerialName("menu_item_id")
    val menuItemId: String = ""
)