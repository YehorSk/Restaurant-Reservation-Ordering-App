package com.yehorsk.platea.menu.presentation.menu

import com.yehorsk.platea.core.presentation.components.CartForm
import com.yehorsk.platea.menu.data.db.model.MenuEntity
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity

data class MenuScreenUiState(
    val currentMenuItem: MenuItemEntity? = null,
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val searchText: String = "",
    val cartForm: CartForm = CartForm(),
    val showMenuDialog: Boolean = false,
    val currentMenu: MenuEntity? = null
)

