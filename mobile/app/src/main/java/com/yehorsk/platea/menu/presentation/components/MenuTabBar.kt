package com.yehorsk.platea.menu.presentation.components

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.menu.data.db.model.MenuEntity

@Composable
fun MenuTabBar(
    modifier: Modifier = Modifier,
    menus: List<MenuEntity>,
    selectedMenu: Int,
    onMenuClicked: (Int, MenuEntity) -> Unit
){
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedMenu,
        edgePadding = 0.dp
    ) {
        menus.forEachIndexed { index, menu ->
            Tab(
                selected = index == selectedMenu,
                onClick = { onMenuClicked(index, menu) },
                text = {
                    Text(
                        text = menu.name,
                        fontSize = 16.sp,
                    )
                }
            )
        }
    }
}