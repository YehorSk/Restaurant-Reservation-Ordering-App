package com.yehorsk.platea.cart.presentation.cart

import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.core.presentation.components.CartForm
import com.yehorsk.platea.menu.domain.models.MenuItem

data class CartScreenUiState(
    val error: String = "",
    val items: List<CartItem> = emptyList(),
    val isNetwork: Boolean = false,
    val currentItem: MenuItem? = null,
    val showBottomSheet: Boolean = false,
    val cartItem: CartItem? = null,
    val isLoading: Boolean = false,
    val cartForm: CartForm = CartForm()
)
