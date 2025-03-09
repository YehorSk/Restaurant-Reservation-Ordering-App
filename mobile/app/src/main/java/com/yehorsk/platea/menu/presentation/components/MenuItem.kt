package com.yehorsk.platea.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun MenuItem(
    menuItem: MenuItemEntity,
    modifier: Modifier = Modifier,
    onClick: (MenuItemEntity) -> Unit
){
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .clickable {
                onClick(menuItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = menuItem.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
            )
            Text(
                text = menuItem.shortDescription,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp),
            )
            Text(
                text = "€"+ formattedPrice(menuItem.price.toDouble()),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 12.dp, start = 16.dp, end = 16.dp, top = 6.dp),
            )
        }

        AsyncImage(
            model = "${menuItem.picture}",
            modifier = Modifier
                .weight(1f)
                .padding(26.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = menuItem.name,
            placeholder = painterResource(R.drawable.menu_item_placeholder),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.menu_item_placeholder)
        )
    }
}

@PreviewLightDark
@Composable
fun MenuItemPreview(){
    MobileTheme {
        MenuItem(
            menuItem = MenuItemEntity(
                id = 0,
                createdAt = "",
                updatedAt = "",
                menuId = 123,
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = 5.99
            ),
            onClick = {},
        )
    }
}