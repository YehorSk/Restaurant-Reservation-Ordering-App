package com.yehorsk.platea.core.domain.remote

sealed interface SideEffect {
    object NavigateToNextScreen : SideEffect
}