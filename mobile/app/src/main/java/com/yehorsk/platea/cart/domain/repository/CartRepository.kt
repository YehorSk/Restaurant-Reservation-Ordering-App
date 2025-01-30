package com.yehorsk.platea.cart.domain.repository

import com.yehorsk.platea.cart.data.remote.dto.CartItemDto
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.presentation.components.CartForm

interface CartRepository {

    suspend fun getAllItems(): Result<List<CartItemDto>, AppError>

    suspend fun addUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError>

    suspend fun deleteUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError>

    suspend fun updateUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError>

}