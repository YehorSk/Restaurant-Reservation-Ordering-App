package com.example.mobile.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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

    object Orders : BottomBarScreen(
        route = "ORDERS",
        title = "ORDERS",
        icon = Icons.Default.ShoppingCart
    )

    object Settings : BottomBarScreen(
        route = "SETTINGS",
        title = "SETTINGS",
        icon = Icons.Default.Settings
    )
}