package com.example.mobile.ui.screens.client.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.menu.data.model.MenuItem

@Composable
fun MenuItem(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Text(
        text = menuItem.name,
        fontSize = 14.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .clickable {
                onClick()
            }
    )
}

@Preview
@Composable
fun MenuItemPreview(){
    MenuItem(
        menuItem = MenuItem(
            id = "0",
            createdAt = "",
            updatedAt = "",
            menuId = "",
            name = "Pancakes",
            description = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
            picture = "",
            price = "5.99"
        ),
        onClick = {}
    )
}