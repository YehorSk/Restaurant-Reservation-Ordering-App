package com.example.mobile.core.data.repository

sealed interface SideEffect {
    data class ShowToast(val message: String) : SideEffect
}