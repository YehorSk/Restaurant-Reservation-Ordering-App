package com.example.mobile.cart.domain.service

import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.core.data.remote.dto.ResponseDto
import com.example.mobile.core.presentation.components.CartForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface CartService {

    @GET("cart/user")
    suspend fun getUserCartItems() : ResponseDto<CartItemDto>

    @POST("cart/user/delete")
    suspend fun deleteUserCartItem(@Body cartForm: CartForm): ResponseDto<CartItemDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("cart/user/add")
    suspend fun addUserCartItem(@Body cartForm: CartForm): ResponseDto<CartItemDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("cart/user/update")
    suspend fun updateUserCartItem(
        @Body cartForm: CartForm
    ): ResponseDto<CartItemDto>

}