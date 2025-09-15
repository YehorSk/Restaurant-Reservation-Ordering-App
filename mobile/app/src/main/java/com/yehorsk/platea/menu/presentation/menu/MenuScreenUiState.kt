package com.yehorsk.platea.menu.presentation.menu

import com.yehorsk.platea.core.presentation.components.CartForm
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem

data class MenuScreenUiState(
    val currentMenuItem: MenuItem? = null,
    val menuItems: List<Menu> = emptyList(),
    val favoriteItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val searchText: String = "",
    val cartForm: CartForm = CartForm(),
    val showMenuDialog: Boolean = false,
    val currentMenu: Menu? = null,
    val isNetwork: Boolean = false
)

