package com.yehorsk.platea.cart.data.remote

import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.mappers.toCartItem
import com.yehorsk.platea.cart.data.remote.dto.CartItemDto
import com.yehorsk.platea.cart.data.remote.dto.toCartItemEntity
import com.yehorsk.platea.cart.data.remote.service.CartService
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.presentation.components.CartForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val cartDao: CartDao
) : CartRepository {

    private suspend fun syncCartItemsWithServer(items: List<CartItemDto>) = withContext(Dispatchers.IO){
        val localItems = cartDao.getAllItemsOnce()
        val serverItems = items.map { it.id }.toSet()
        val itemsToDelete = localItems.filter { it.id !in serverItems}
        cartDao.deleteItems(itemsToDelete)
        insertItems(items)
    }

    private suspend fun insertItems(items: List<CartItemDto>){
        for(item in items){
            cartDao.insertItem(item.toCartItemEntity())
        }
    }

    override suspend fun getAllItems(): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart getAllItems")
        return safeCall<CartItemDto>(
            execute = {
                cartService.getUserCartItems()
            }
        ).onSuccess{ data, _ ->
            Timber.d("refreshData $data")
            syncCartItemsWithServer(data)
        }
    }

    override suspend fun addUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart addUserCartItem")
        return safeCall<CartItemDto>(
            execute = {
                cartService.addUserCartItem(cartForm)
            }
        ).onSuccess { data, _ ->
            cartDao.insertItem(data.first().toCartItemEntity())
        }
    }

    override suspend fun deleteUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart deleteUserCartItem")
        return safeCall<CartItemDto>(
            execute = {
                cartService.deleteUserCartItem(cartForm)
            }
        ).onSuccess { data, _ ->
            cartDao.deleteItem(data.first().toCartItemEntity())
        }
    }

    override suspend fun updateUserCartItem(cartForm: CartForm): Result<List<CartItemDto>, AppError> {
        Timber.d("Cart updateUserCartItem")
        return safeCall<CartItemDto>(
            execute = {
                cartService.updateUserCartItem(cartForm)
            }
        ).onSuccess { data, _ ->
            cartDao.updateItem(data.first().toCartItemEntity())
        }
    }

    override fun getAllItemsFlow(): Flow<List<CartItem>> {
        return cartDao
            .getAllItems()
            .map { data ->
                data.map {
                    it.toCartItem()
                }
            }
    }

    override fun getAmountOfItems(): Flow<Int> {
        return cartDao.getAmountOfItems()
    }
}