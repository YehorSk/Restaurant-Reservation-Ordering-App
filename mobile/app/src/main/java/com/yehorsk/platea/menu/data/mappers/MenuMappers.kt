package com.yehorsk.platea.menu.data.mappers

import com.yehorsk.platea.menu.data.db.model.MenuEntity
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import com.yehorsk.platea.menu.data.remote.dto.MenuItemDto
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem

fun MenuEntity.toMenu() = Menu(
    id = id,
    name = name,
    description = description,
    availability = availability
)

fun MenuDto.toMenu() = Menu(
    id = id,
    name = name,
    description = description,
    availability = availability
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
    isFavorite = isFavorite
)