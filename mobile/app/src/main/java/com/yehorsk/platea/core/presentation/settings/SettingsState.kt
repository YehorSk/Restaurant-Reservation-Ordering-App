package com.yehorsk.platea.core.presentation.settings

data class SettingsState (
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val isPhoneValid: Boolean = false,
    val code: String = "",
    val phone: String = "",
    val address: String = "",
    val name: String = ""
)