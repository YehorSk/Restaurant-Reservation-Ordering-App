package com.yehorsk.platea.menu.domain.models

data class MenuItem(
    val createdAt: String,
    val updatedAt: String,
    val id: Int,
    val menuId: String,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val recipe: String,
    val picture: String,
    val price: Double,
    val availability: Boolean,
    val isFavorite: Boolean = false
)
