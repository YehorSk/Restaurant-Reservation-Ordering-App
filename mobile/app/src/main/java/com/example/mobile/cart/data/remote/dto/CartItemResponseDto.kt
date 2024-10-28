package com.example.mobile.cart.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponseDto(
    val status: String?,
    @SerialName("text_status")
    val textStatus: String?,
    val message: String?,
    val data: CartItem,
)
