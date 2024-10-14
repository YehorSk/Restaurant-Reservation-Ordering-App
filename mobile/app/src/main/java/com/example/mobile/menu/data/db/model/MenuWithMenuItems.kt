package com.example.mobile.menu.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class MenuWithMenuItems(

    @Embedded
    val menu: MenuEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "menu_id"
    )
    val menuItems: List<MenuItemEntity>

)
