package com.example.mobile.cart.data.remote

import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.utils.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val prefs: MainPreferencesRepository,
    private val networkConnectivityObserver: ConnectivityObserver,
    private val cartDao: CartDao
) : CartRepository {

    private val isOnlineFlow: StateFlow<Boolean> = networkConnectivityObserver.observe()
        .distinctUntilChanged()
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    override suspend fun getAllItems(): NetworkResult<List<CartItemDto>> {
        Timber.d("Cart getAllItems")
        return safeCall<CartItemDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                cartService.getUserCartItems()
            }
        )
    }

    override suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<List<CartItemDto>> {
        Timber.d("Cart addUserCartItem")
        return safeCall<CartItemDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                cartService.addUserCartItem(cartForm)
            },
            onSuccess = { data ->
                cartDao.insertItem(data.first().toCartItemEntity())
            }
        )
    }

    override suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<List<CartItemDto>> {
        Timber.d("Cart deleteUserCartItem")
        return safeCall<CartItemDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                cartService.deleteUserCartItem(cartForm)
            },
            onSuccess = { data ->
                cartDao.deleteItem(data.first().toCartItemEntity())
            }
        )
    }

    override suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<List<CartItemDto>> {
        Timber.d("Cart updateUserCartItem")
        return safeCall<CartItemDto>(
            isOnlineFlow = isOnlineFlow,
            execute = {
                cartService.updateUserCartItem(cartForm)
            },
            onSuccess = { data ->
                cartDao.updateItem(data.first().toCartItemEntity())
            },
        )
    }
}