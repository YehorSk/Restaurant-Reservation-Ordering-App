package com.example.mobile.auth.presentation.forgot

import kotlinx.serialization.Serializable

data class ForgotState(
    val form: ForgotFormState = ForgotFormState(),
    val isEntryValid: Boolean = false,
    val internetError: Boolean = false,
    val isLoading: Boolean = false
)

@Serializable
data class ForgotFormState(
    val email: String = ""
)
