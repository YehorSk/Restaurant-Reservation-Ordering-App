package com.example.mobile.core.data.remote

sealed interface UserRoles{
    object USER: UserRoles
    object ADMIN: UserRoles
    object WAITER: UserRoles
    object CHEF: UserRoles
}
