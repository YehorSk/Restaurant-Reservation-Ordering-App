package com.yehorsk.platea.core.utils

import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.presentation.BottomBarScreen
import kotlin.collections.listOf

fun getUserBarItems(userRole: UserRoles): List<BottomBarScreen>{
    return when(userRole) {
        UserRoles.USER -> {
            listOf(
                BottomBarScreen.Home,
                BottomBarScreen.Cart,
                BottomBarScreen.Favorites,
                BottomBarScreen.Account
            )
        }
        UserRoles.ADMIN -> {
            listOf(
                BottomBarScreen.Orders,
                BottomBarScreen.Reservations,
                BottomBarScreen.Account
            )
        }
        UserRoles.CHEF -> {
            listOf(
                BottomBarScreen.Orders,
                BottomBarScreen.Account,
            )
        }
        UserRoles.WAITER -> {
            listOf(
                BottomBarScreen.Home,
                BottomBarScreen.Cart,
                BottomBarScreen.Favorites,
                BottomBarScreen.Account
            )
        }
        UserRoles.AUTH -> {
            listOf()
        }
    }
}