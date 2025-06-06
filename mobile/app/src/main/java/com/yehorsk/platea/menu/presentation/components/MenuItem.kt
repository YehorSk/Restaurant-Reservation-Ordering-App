package com.yehorsk.platea.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yehorsk.platea.BuildConfig
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.menu.domain.models.MenuItem
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun MenuItem(
    menuItem: MenuItem,
    modifier: Modifier = Modifier,
    onClick: (MenuItem) -> Unit
){
    val imgUrl = BuildConfig.BASE_URL_IMG
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
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 12.dp, start = 16.dp, end = 16.dp, top = 12.dp),
            )
        }

        Box(
            modifier = Modifier
                .height(100.dp),
        ){
            AsyncImage(
                model = "$imgUrl${menuItem.picture}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(
                        ratio = (3f / 2f),
                        matchHeightConstraintsFirst = true,
                    )
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
                    .clip(RoundedCornerShape(10.dp)),
                contentDescription = menuItem.name,
                placeholder = painterResource(R.drawable.menu_item_placeholder),
                error = painterResource(R.drawable.menu_item_placeholder),
            )
        }
    }
}

@Preview
@Composable
fun MenuItemPreview(){
    MobileTheme {
        MenuItem(
            menuItem = MenuItem(
                id = 0,
                createdAt = "",
                updatedAt = "",
                menuId = "123",
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = 5.99,
                availability = true
            ),
            onClick = {},
        )
    }
}