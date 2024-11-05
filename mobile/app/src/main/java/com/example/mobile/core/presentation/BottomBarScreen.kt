package com.example.mobile.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "HOME",
        title = "HOME",
        icon = Icons.Default.Home
    )

    object Cart : BottomBarScreen(
        route = "CART",
        title = "CART",
        icon = Icons.Default.ShoppingCart
    )

    object Orders : BottomBarScreen(
        route = "ORDERS",
        title = "ORDERS",
        icon = Icons.Default.Receipt
    )

    object Settings : BottomBarScreen(
        route = "SETTINGS",
        title = "SETTINGS",
        icon = Icons.Default.Settings
    )

    object Favorites : BottomBarScreen(
        route = "FAVORITES",
        title = "FAVORITES",
        icon = Icons.Default.Favorite
    )

}