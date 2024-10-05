package com.example.mobile.ui.screens.auth.forgot

import kotlinx.serialization.Serializable

data class ForgotState(
    val form: StateForm = StateForm(),
    val isEntryValid: Boolean = false,
    val internetError: Boolean = false,
)

@Serializable
data class StateForm(
    val email: String = ""
)
