package com.example.mobile.cart.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.Alignment
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
import com.example.mobile.core.utils.formattedPrice
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
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .clickable {
                onClick(cartItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Column(
            modifier = Modifier
                .weight(1f)
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
                color = contentColor

            )
            Text(
                text = stringResource(R.string.quantity, cartItem.pivot.quantity),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                color = contentColor
            )
            Text(
                text = "€"+ formattedPrice(cartItem.pivot.price),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp),
                color = contentColor
            )
        }
        AsyncImage(
            model = cartItem.picture,
            modifier = Modifier
                .weight(1f)
                .padding(32.dp)
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