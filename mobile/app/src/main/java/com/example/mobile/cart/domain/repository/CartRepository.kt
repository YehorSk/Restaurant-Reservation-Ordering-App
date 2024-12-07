package com.example.mobile.cart.domain.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto

interface CartRepository {

    suspend fun getAllItems(): NetworkResult<List<CartItemDto>>

    suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<List<CartItemDto>>

    suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<List<CartItemDto>>

    suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<List<CartItemDto>>

}