package com.example.mobile.cart.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.db.model.PivotCartItemEntity
import com.example.mobile.ui.theme.MobileTheme
import timber.log.Timber

@Composable
fun CartItem(
    cartItem: CartItemEntity,
    modifier: Modifier = Modifier,
    onClick: (CartItemEntity) -> Unit
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }

    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .clickable {
                Timber.d("Item $cartItem")
                onClick(cartItem)
            }
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = cartItem.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = contentColor

            )
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.quantity, cartItem.pivot.quantity),
                    fontSize = 14.sp,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = contentColor
                )
                Text(
                    text = stringResource(R.string.price, cartItem.pivot.price),
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = contentColor
                )
            }
        }
        AsyncImage(
            model = cartItem.picture,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = cartItem.name,
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
        CartItem(
            cartItem = CartItemEntity(
                id = 0,
                menuId = 0,
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = 5.99,
                createdAt = "",
                updatedAt = "",
                pivot = PivotCartItemEntity(
                    userId = 0,
                    menuItemId = 0,
                    price = 1000.0,
                    quantity = 20,
                    createdAt = "",
                    updatedAt = "",
                    id=0,
                ),
                isFavorite = false
            ),
            onClick = {}
        )
    }
}