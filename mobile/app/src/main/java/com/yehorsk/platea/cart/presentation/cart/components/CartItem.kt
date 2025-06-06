package com.yehorsk.platea.cart.presentation.cart.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yehorsk.platea.BuildConfig
import com.yehorsk.platea.R
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.cart.domain.models.PivotCartItem
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun CartItem(
    cartItem: CartItem,
    modifier: Modifier = Modifier,
    onClick: (CartItem) -> Unit
){
    val imgUrl = BuildConfig.BASE_URL_IMG
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .clickable {
                onClick(cartItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = cartItem.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
            Text(
                text = stringResource(R.string.quantity, cartItem.pivot.quantity),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            )
            Text(
                text = "€"+ formattedPrice(cartItem.pivot.price),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp),
            )
        }
        Box(
            modifier = Modifier
                .height(100.dp),
        ){
            AsyncImage(
                model = "$imgUrl${cartItem.picture}",
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
                contentDescription = cartItem.name,
                placeholder = painterResource(R.drawable.menu_item_placeholder),
                error = painterResource(R.drawable.menu_item_placeholder)
            )
        }
    }
}

@Preview
@Composable
fun MenuItemPreview(){
    MobileTheme {
        CartItem(
            cartItem = CartItem(
                id = 0,
                menuId = "0",
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = 5.99,
                createdAt = "",
                updatedAt = "",
                pivot = PivotCartItem(
                    userId = 0,
                    menuItemId = 0,
                    price = 1000.0,
                    quantity = 20,
                    createdAt = "",
                    updatedAt = "",
                    id=0,
                ),
                isFavorite = false,
                availability = true
            ),
            onClick = {}
        )
    }
}