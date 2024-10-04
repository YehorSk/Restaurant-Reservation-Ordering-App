package com.example.mobile.common

sealed interface SideEffect {
    data class ShowToast(val message: String) : SideEffect
}