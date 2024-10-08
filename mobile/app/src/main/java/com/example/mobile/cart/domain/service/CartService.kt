package com.example.mobile.cart.domain.service

import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.model.CartItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CartService {

    @GET("user-cart-items")
    suspend fun getUserCartItems(@Header("Authorization") token: String) : List<CartItem>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("delete-user-cart-item")
    suspend fun deleteUserCartItem(@Header("Authorization") token: String, @Body cartForm: CartForm): String

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("update-user-cart-item")
    suspend fun updateUserCartItem(@Header("Authorization") token: String, @Body cartForm: CartForm): String

}