package com.example.mobile.cart.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCartItem(
    val status: String?,
    val message: String?,
    val data: CartItem,
)
