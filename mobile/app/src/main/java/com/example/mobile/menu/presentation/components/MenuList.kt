package com.example.mobile.menu.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.db.model.MenuWithMenuItems

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuList(
    modifier: Modifier = Modifier,
    items: List<MenuWithMenuItems>,
    onClick: (MenuItemEntity) -> Unit
){
    LazyColumn(
        modifier = modifier
    ) {
        items.forEach { menu ->
            stickyHeader {
                MenuHeader(
                    menuDto = menu.menu,
                    onMenuClick = {  }
                )
            }
            items(menu.menuItems.map { it }){ item ->
                MenuItem(
                    menuItem = item,
                    onClick = { menuItem ->
                        onClick(menuItem)
                    }
                )
                HorizontalDivider()
            }
        }
    }
}