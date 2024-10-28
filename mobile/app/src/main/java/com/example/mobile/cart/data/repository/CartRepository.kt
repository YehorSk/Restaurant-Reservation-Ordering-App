package com.example.mobile.cart.data.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItem

interface CartRepository {

    suspend fun getAllItems(): NetworkResult<List<CartItem>>

    suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<CartItem>

    suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<CartItem>

    suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<CartItem>

}