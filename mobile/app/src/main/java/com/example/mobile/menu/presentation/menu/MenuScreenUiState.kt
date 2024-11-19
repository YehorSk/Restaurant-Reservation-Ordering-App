package com.example.mobile.menu.presentation.menu

import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.data.db.model.MenuItemEntity

data class MenuScreenUiState(
    val currentMenu: MenuItemEntity? = null,
    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val searchText: String = "",
    val cartForm: CartForm = CartForm()
)

