package com.example.mobile.core.presentation.settings

sealed class ProfileDestination {
    object Favorites : ProfileDestination()
    object Settings : ProfileDestination()
    object Logout : ProfileDestination()
}
