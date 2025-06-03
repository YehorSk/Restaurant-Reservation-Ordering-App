package com.yehorsk.platea.cart.presentation.cart

import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.menu.domain.models.MenuItem

sealed interface CartAction {

    data class SetItem(val item: CartItem): CartAction

    data class SetMenuItem(val item: MenuItem): CartAction

    data object GetItems: CartAction

    data object DeleteItem: CartAction

    data object ShowBottomSheet: CartAction

    data object CloseBottomSheet: CartAction

    data object ClearForm: CartAction

    data class UpdateQuantity(val quantity: Int): CartAction

    data class UpdatePrice(val price: Double): CartAction

    data object UpdateItem: CartAction

}