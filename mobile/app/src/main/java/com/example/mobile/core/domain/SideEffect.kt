package com.example.mobile.core.domain

sealed interface SideEffect {
    data class ShowSuccessToast(val message: String) : SideEffect
    data class ShowErrorToast(val message: AppError) : SideEffect
    object NavigateToNextScreen : SideEffect
}