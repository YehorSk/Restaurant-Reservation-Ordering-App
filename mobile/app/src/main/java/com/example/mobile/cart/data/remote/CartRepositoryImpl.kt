package com.example.mobile.cart.data.remote

import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val cartDao: CartDao
) : CartRepository {

    override suspend fun getAllItems(): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart getAllItems")
        return safeCall<CartItemDto>(
            execute = {
                cartService.getUserCartItems()
            }
        )
    }

    override suspend fun addUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart addUserCartItem")
        return safeCall<CartItemDto>(
            execute = {
                cartService.addUserCartItem(cartForm)
            },
            onSuccess = { data ->
                cartDao.insertItem(data.first().toCartItemEntity())
            }
        )
    }

    override suspend fun deleteUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart deleteUserCartItem")
        return safeCall<CartItemDto>(
            execute = {
                cartService.deleteUserCartItem(cartForm)
            },
            onSuccess = { data ->
                cartDao.deleteItem(data.first().toCartItemEntity())
            }
        )
    }

    override suspend fun updateUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart updateUserCartItem")
        return safeCall<CartItemDto>(
            execute = {
                cartService.updateUserCartItem(cartForm)
            },
            onSuccess = { data ->
                cartDao.updateItem(data.first().toCartItemEntity())
            },
        )
    }
}