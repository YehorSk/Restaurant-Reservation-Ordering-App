package com.yehorsk.platea.cart.data.remote

import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.remote.dto.CartItemDto
import com.yehorsk.platea.cart.data.remote.dto.toCartItemEntity
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.cart.domain.service.CartService
import com.yehorsk.platea.core.data.remote.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.presentation.components.CartForm
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