package com.yehorsk.platea.cart.data.mappers

import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.cart.data.db.model.PivotCartItemEntity
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.cart.domain.models.PivotCartItem
import com.yehorsk.platea.menu.domain.models.MenuItem

fun CartItemEntity.toMenuItem() = MenuItem(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    menuId = this.menuId,
    name = this.name,
    shortDescription = this.shortDescription,
    longDescription = this.longDescription,
    recipe = this.recipe,
    picture = this.picture,
    price = this.price,
    isFavorite = this.isFavorite,
    availability = this.availability
)

fun CartItem.toMenuItem() = MenuItem(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    menuId = this.menuId,
    name = this.name,
    shortDescription = this.shortDescription,
    longDescription = this.longDescription,
    recipe = this.recipe,
    picture = this.picture,
    price = this.price,
    isFavorite = this.isFavorite,
    availability = this.availability
)

fun PivotCartItemEntity.toPivotCartItem() = PivotCartItem(
    id = id,
    userId = userId,
    menuItemId = menuItemId,
    quantity = quantity,
    price = price,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CartItemEntity.toCartItem() = CartItem(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    menuId = this.menuId,
    name = this.name,
    shortDescription = this.shortDescription,
    longDescription = this.longDescription,
    recipe = this.recipe,
    picture = this.picture,
    price = this.price,
    isFavorite = this.isFavorite,
    availability = this.availability,
    pivot = this.pivot.toPivotCartItem()
)

