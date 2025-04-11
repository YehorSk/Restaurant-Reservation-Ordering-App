package com.yehorsk.platea.core.utils

import com.yehorsk.platea.core.domain.remote.AppError

sealed interface SideEffect {
    data class ShowSuccessToast(val message: String) : SideEffect
    data class ShowErrorToast(val message: AppError) : SideEffect
    object NavigateToNextScreen : SideEffect
    object LanguageChanged: SideEffect
}