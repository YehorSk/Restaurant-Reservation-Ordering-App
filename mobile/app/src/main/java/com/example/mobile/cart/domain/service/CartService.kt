package com.example.mobile.cart.domain.service

import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.core.data.remote.dto.ResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface CartService {

    @GET("user-cart-items")
    suspend fun getUserCartItems() : ResponseDto<CartItemDto>

    @POST("delete-user-cart-item")
    suspend fun deleteUserCartItem(@Body cartForm: CartForm): ResponseDto<CartItemDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("add-user-cart-item")
    suspend fun addUserCartItem(@Body cartForm: CartForm): ResponseDto<CartItemDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("update-user-cart-item")
    suspend fun updateUserCartItem(
        @Body cartForm: CartForm
    ): ResponseDto<CartItemDto>

}