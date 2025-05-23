package com.yehorsk.platea.menu.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuList(
    modifier: Modifier = Modifier,
    items: List<MenuWithMenuItems>,
    onClick: (MenuItemEntity) -> Unit,
    listState: LazyListState = rememberLazyListState(),
){
    Column{
        LazyColumn(
            state = listState,
            modifier = modifier
        ) {
            itemsIndexed(items){ _, menu ->
                if(menu.menu.availability){
                    MenuHeader(
                        menuDto = menu.menu,
                        onMenuClick = {  }
                    )
                    var i = 0;
                    Column {
                        menu.menuItems.forEach{ item ->
                            MenuItem(
                                menuItem = item,
                                onClick = { menuItem ->
                                    onClick(menuItem)
                                }
                            )
                            if(i++ != menu.menuItems.size -1) HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}