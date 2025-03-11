package com.yehorsk.platea.orders.presentation.order_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yehorsk.platea.BuildConfig
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.orders.data.db.model.OrderItemEntity
import com.yehorsk.platea.orders.data.db.model.PivotOrderItemEntity
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun OrderDetailsListItem(
    menuItem: OrderItemEntity,
    modifier: Modifier = Modifier,
    onClick: (OrderItemEntity) -> Unit,
){
    val imgUrl = BuildConfig.BASE_URL_IMG
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .height(100.dp)
            .clickable {
                onClick(menuItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        AsyncImage(
            model = "$imgUrl${menuItem.picture}",
            modifier = Modifier
                .padding(12.dp)
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = menuItem.name,
            placeholder = painterResource(R.drawable.menu_item_placeholder),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.menu_item_placeholder)
        )
        Column(
            modifier = Modifier
        ) {
            Text(
                text = menuItem.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
            Text(
                text =  menuItem.pivot.quantity.toString() +"x "+ " €"+ formattedPrice(menuItem.pivot.price.toDouble()),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 8.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
fun OrderDetailsListItemPreview(){
    MobileTheme {
        OrderDetailsListItem(
            menuItem = OrderItemEntity(
                id = 0,
                createdAt = "",
                updatedAt = "",
                menuId = 123,
                name = "Pancakes",
                longDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                shortDescription = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                recipe = "Fluffy pancakes with maple syrup, butter, and fresh fruit",
                picture = "",
                price = 5.99,
                pivot = PivotOrderItemEntity(
                    id = 1,
                    orderId = 10,
                    menuItemId = 1,
                    quantity = 2,
                    price = 17.98,
                    createdAt = "2024-01-01T10:00:00Z",
                    updatedAt = "2024-01-01T10:05:00Z"
                )
            ),
            onClick = {}
        )
    }
}