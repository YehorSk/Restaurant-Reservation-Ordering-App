package com.yehorsk.platea.menu.presentation

import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem

sealed interface MenuAction {

    data object AddCartItem: MenuAction

    data object AddFavoriteItem: MenuAction

    data object DeleteFavoriteItem: MenuAction

    data class ShowMenuDetails(val menu: Menu): MenuAction

    data object HideMenuDetails: MenuAction

    data object CloseBottomSheet: MenuAction

    data object ClearForm: MenuAction

    data class UpdateQuantity(val quantity: Int): MenuAction

    data class UpdatePrice(val price: Double): MenuAction

    data class SetMenuFavoriteItem(val value: Boolean): MenuAction

    data class OnMenuItemClick(val item: MenuItem): MenuAction

}