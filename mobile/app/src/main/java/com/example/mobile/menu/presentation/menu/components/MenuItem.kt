package com.example.mobile.menu.presentation.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.menu.data.remote.model.MenuItem
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun MenuItem(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onClick: (MenuItem) -> Unit
){
    Row(
        modifier = Modifier.background(Color.White)
            .height(140.dp)
            .clickable {
                onClick(menuItem)
            }
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = menuItem.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)

            )
            Text(
                text = menuItem.shortDescription,
                fontSize = 12.sp,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            Text(
                text = "€"+menuItem.price,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp)
            )
        }
        AsyncImage(
            model = menuItem.picture,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = menuItem.name,
            placeholder = painterResource(R.drawable.menu_item_placeholder),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.menu_item_placeholder)
        )
    }
}

@Preview
@Composable
fun MenuItemPreview(){
    MobileTheme {
        MenuItem(
            menuItem = MenuItem(
                id = "0",
                createdAt = "",
                updatedAt = "",
                menuId = "",
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = "5.99"
            ),
            onClick = {}
        )
    }
}