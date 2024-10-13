package com.example.mobile.cart.presentation.cart.components

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
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.db.model.PivotCartItemEntity
import com.example.mobile.cart.data.remote.model.CartItem
import com.example.mobile.cart.data.remote.model.Pivot
import com.example.mobile.ui.theme.MobileTheme
import timber.log.Timber

@Composable
fun CartItem(
    cartItem: CartItemEntity,
    modifier: Modifier = Modifier,
    onClick: (CartItemEntity) -> Unit
){
    Row(
        modifier = Modifier.background(Color.White)
            .height(160.dp)
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
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)

            )
            Text(
                text = cartItem.shortDescription,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            if(cartItem.pivot.note.isNotEmpty()){
                Text(
                    text = cartItem.pivot.note,
                    fontSize = 12.sp,
                    maxLines = 2,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                )
            }
            Row(
                modifier = modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp)
            ) {
                Text(
                    text = cartItem.pivot.quantity.toString()+"x",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Text(
                    text = cartItem.pivot.price.toString()+ " €",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
        AsyncImage(
            model = cartItem.picture,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = cartItem.name,
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
        CartItem(
            cartItem = CartItemEntity(
                id = "0",
                menuId = "",
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = 5.99,
                createdAt = "",
                updatedAt = "",
                pivot = PivotCartItemEntity(
                    userId = "0",
                    menuItemId = "0",
                    price = 100.0,
                    quantity = 20,
                    createdAt = "",
                    updatedAt = "",
                    note = "Hello world",
                    id=""
                )
            ),
            onClick = {}
        )
    }
}