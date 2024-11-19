package com.example.mobile.cart.presentation.cart

import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.data.db.model.MenuItemEntity

data class CartScreenUiState(
    val error: String = "",
    val currentItem: MenuItemEntity? = null,
    val cartItem: CartItemEntity? = null,
    val isLoading: Boolean = false,
    val cartForm: CartForm = CartForm()
)
