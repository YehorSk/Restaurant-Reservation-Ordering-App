package com.yehorsk.platea.menu.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.menu.domain.models.Menu

@Composable
fun MenuTabBar(
    modifier: Modifier = Modifier,
    menus: List<Menu>,
    selectedMenu: Int,
    onMenuClicked: (Int, Menu) -> Unit
){
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedMenu,
        edgePadding = 0.dp
    ) {
        menus.forEachIndexed { index, menu ->
            if(menu.availability){
                Tab(
                    selected = index == selectedMenu,
                    onClick = { onMenuClicked(index, menu) },
                    text = {
                        Text(
                            text = menu.name,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                )
            }
        }
    }
}