package com.example.mobile.cart.domain.repository

import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result

interface CartRepository {

    suspend fun getAllItems(): Result<List<CartItemDto>, AppError>

    suspend fun addUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError>

    suspend fun deleteUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError>

    suspend fun updateUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError>

}