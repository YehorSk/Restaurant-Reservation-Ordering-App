package com.example.mobile.menu.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class MenuWithMenuItems(

    @Embedded val user: MenuEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val menuItems: List<MenuItemEntity>

)
