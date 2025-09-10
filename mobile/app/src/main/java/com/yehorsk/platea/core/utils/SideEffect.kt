package com.yehorsk.platea.core.utils

sealed interface SideEffect {
    object NavigateToNextScreen : SideEffect
}