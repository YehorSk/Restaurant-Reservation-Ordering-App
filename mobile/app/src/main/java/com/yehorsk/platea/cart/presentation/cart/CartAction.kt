package com.yehorsk.platea.cart.presentation.cart

import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity

sealed interface CartAction {

    data class SetItem(val item: CartItemEntity): CartAction

    data class SetMenuItem(val item: MenuItemEntity): CartAction

    data object DeleteItem: CartAction

    data object ShowBottomSheet: CartAction

    data object CloseBottomSheet: CartAction

    data object ClearForm: CartAction

    data class UpdateQuantity(val quantity: Int): CartAction

    data class UpdatePrice(val price: Double): CartAction

    data object UpdateItem: CartAction

}