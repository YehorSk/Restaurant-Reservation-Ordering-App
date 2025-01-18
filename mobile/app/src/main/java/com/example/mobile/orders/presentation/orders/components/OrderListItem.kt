package com.example.mobile.orders.presentation.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.utils.formatOrderDateTime
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun OrderListItem(
    modifier: Modifier = Modifier,
    orderEntity: OrderEntity,
    onGoToOrderDetails: (Int) -> Unit
){
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable {
                onGoToOrderDetails(orderEntity.id.toInt())
            }
    ){
        Column {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = "#${orderEntity.code}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                text = "€${formattedPrice(orderEntity.price)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            ) {
                Text(
                    text = formatOrderDateTime(orderEntity.createdAt),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                )
                Text(
                    text = " ${orderEntity.status}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview
@Composable
fun OrderItemPreview(){
    val fakeOrder = OrderEntity(
        id = "order_12345",
        createdAt = "2024-12-01T12:34:56",
        updatedAt = "2024-12-01T12:45:00",
        clientId = 101,
        tableId = 15,
        waiterId = 3,
        reservationId = 55,
        price = 45.99,
        status = "PREPARING",
        specialRequest = "Extra spicy",
        orderType = 1,
        code = "F6JKN1"
    )
    MobileTheme {
        OrderListItem(
            orderEntity = fakeOrder,
            onGoToOrderDetails = {}
        )
    }
}