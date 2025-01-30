package com.yehorsk.platea.cart.presentation.cart

import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.core.presentation.components.CartForm
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity

data class CartScreenUiState(
    val error: String = "",
    val currentItem: MenuItemEntity? = null,
    val showBottomSheet: Boolean = false,
    val cartItem: CartItemEntity? = null,
    val isLoading: Boolean = false,
    val cartForm: CartForm = CartForm()
)
