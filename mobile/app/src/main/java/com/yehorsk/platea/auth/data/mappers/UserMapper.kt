package com.yehorsk.platea.auth.data.mappers

import com.yehorsk.platea.auth.data.remote.models.UserDto
import com.yehorsk.platea.auth.domain.models.User

fun UserDto.toUser() = User(
    id = this.id,
    name = this.name,
    email = this.email,
    role = this.role,
    address = this.address,
    language = this.language,
    phone = this.phone,
    countryCode = this.countryCode
)