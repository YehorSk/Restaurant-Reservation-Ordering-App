package com.yehorsk.platea.core.presentation.components

import kotlinx.serialization.Serializable

@Serializable
data class MenuForm(
    val name: String,
    val description: String,
    val availability: Boolean,
)
