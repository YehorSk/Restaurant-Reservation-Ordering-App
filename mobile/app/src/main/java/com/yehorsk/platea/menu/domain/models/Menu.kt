package com.yehorsk.platea.menu.domain.models

data class Menu(
    val createdAt: String,
    val updatedAt: String,
    val id: String,
    val name: String,
    val description: String,
    val availability: Boolean,
    val items: List<MenuItem>
)
