package com.yehorsk.platea.menu.data.mappers

import com.yehorsk.platea.menu.data.db.model.MenuEntity
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import com.yehorsk.platea.menu.data.remote.dto.MenuItemDto
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem

fun MenuWithMenuItems.toMenu() = Menu(
    id = menu.id,
    name = menu.name,
    description = menu.description,
    availability = menu.availability,
    items = menuItems.map { it.toMenuItem() },
    createdAt = menu.createdAt,
    updatedAt = menu.updatedAt
)

fun MenuDto.toMenu() = Menu(
    id = id,
    name = name,
    description = description,
    availability = availability,
    createdAt = createdAt,
    updatedAt = updatedAt,
    items = items.map { it.toMenuItem() }
)

fun MenuItemEntity.toMenuItem() = MenuItem(
    id = id,
    menuId = menuId,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    recipe = recipe,
    picture = picture,
    price = price,
    availability = availability,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isFavorite = isFavorite
)

fun MenuItemDto.toMenuItem() = MenuItem(
    id = id,
    menuId = menuId,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    recipe = recipe,
    picture = picture,
    price = price,
    availability = availability,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isFavorite = isFavorite
)

fun MenuItem.toMenuItemDto() = MenuItemDto(
    id = id,
    menuId = menuId,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    recipe = recipe,
    picture = picture,
    price = price,
    availability = availability,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isFavorite = isFavorite
)