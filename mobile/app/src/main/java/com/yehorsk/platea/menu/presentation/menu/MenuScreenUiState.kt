package com.yehorsk.platea.menu.presentation.menu

import com.yehorsk.platea.core.presentation.components.CartForm
import com.yehorsk.platea.menu.data.db.model.MenuEntity
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems

data class MenuScreenUiState(
    val menuWithMenuItems: List<MenuWithMenuItems> = emptyList(),
    val favoriteItems: List<MenuItemEntity> = emptyList(),
    val currentMenuItem: MenuItemEntity? = null,
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val searchText: String = "",
    val cartForm: CartForm = CartForm(),
    val showMenuDialog: Boolean = false,
    val currentMenu: MenuEntity? = null
)

