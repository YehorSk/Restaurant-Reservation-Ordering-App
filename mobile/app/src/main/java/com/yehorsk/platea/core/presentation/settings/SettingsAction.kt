package com.yehorsk.platea.core.presentation.settings

sealed interface SettingsAction{

    data object ShowDeleteDialog : SettingsAction

    data object HideDeleteDialog : SettingsAction

    data object GetRestaurantInfo : SettingsAction

    data object DeleteAccount : SettingsAction

    data object Logout : SettingsAction

    data class ValidatePhoneNumber(val phone: String) : SettingsAction

    data class UpdateTheme(val value: Boolean) : SettingsAction

    data class UpdateLanguage(val value: String) : SettingsAction

    data class UpdateProfile(
        val name: String,
        val address: String,
        val phone: String,
        val code: String
    ) : SettingsAction

}