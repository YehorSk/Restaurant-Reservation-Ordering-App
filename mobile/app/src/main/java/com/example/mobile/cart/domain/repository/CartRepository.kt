package com.example.mobile.cart.domain.repository

import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.model.CartItem

interface CartRepository {

    suspend fun getAllItems(): NetworkResult<List<CartItem>>

    suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<String>

    suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<String>

}