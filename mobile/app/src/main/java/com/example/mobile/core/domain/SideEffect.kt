package com.example.mobile.core.domain

sealed interface SideEffect {
    data class ShowToast(val message: String, val code:Int = 200) : SideEffect
    object NavigateToNextScreen : SideEffect
}