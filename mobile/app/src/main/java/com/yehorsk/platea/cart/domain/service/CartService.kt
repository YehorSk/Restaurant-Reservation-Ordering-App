package com.yehorsk.platea.cart.domain.service

import com.yehorsk.platea.cart.data.remote.dto.CartItemDto
import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.core.presentation.components.CartForm
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