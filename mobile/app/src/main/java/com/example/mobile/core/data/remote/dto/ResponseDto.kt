package com.example.mobile.core.data.remote.dto

import com.example.mobile.cart.data.remote.dto.CartItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val status: String?,
    @SerialName("text_status")
    val textStatus: String?,
    val message: String?,
    val data: T? = null,
)
