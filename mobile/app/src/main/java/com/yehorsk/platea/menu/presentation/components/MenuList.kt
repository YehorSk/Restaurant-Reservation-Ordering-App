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
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuList(
    modifier: Modifier = Modifier,
    items: List<Menu>,
    onClick: (MenuItem) -> Unit,
    onMenuClick: (Menu) -> Unit,
    listState: LazyListState = rememberLazyListState(),
){
    Column{
        LazyColumn(
            state = listState,
            modifier = modifier
        ) {
            itemsIndexed(items){ _, menu ->
                if(menu.availability){
                    MenuHeader(
                        menu = menu,
                        onMenuClick = { onMenuClick(menu) }
                    )
                    var i = 0;
                    Column {
                        menu.items.forEach{ item ->
                            MenuItem(
                                menuItem = item,
                                onClick = { menuItem ->
                                    onClick(menuItem)
                                }
                            )
                            if(i++ != menu.items.size -1) HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}