package com.example.mobile.core.presentation.settings

sealed class ProfileDestination {
    object Favorites : ProfileDestination()
    object Profile : ProfileDestination()
    object Reservations : ProfileDestination()
    object Theme : ProfileDestination()
    object Language : ProfileDestination()
    object Orders : ProfileDestination()
    object Logout : ProfileDestination()
}
