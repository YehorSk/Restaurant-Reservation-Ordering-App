package com.yehorsk.platea.auth.domain.models

data class User(
    val id: Int? = 0,
    val name: String? = "",
    val email: String? = "",
    val role: String? = "",
    val address: String? = "",
    val language: String? = "",
    val phone: String? = "",
    val countryCode: String? = ""
)
