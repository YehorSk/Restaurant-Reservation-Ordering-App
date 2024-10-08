package com.example.mobile.core.domain.repository

sealed interface SideEffect {
    data class ShowToast(val message: String) : SideEffect
}