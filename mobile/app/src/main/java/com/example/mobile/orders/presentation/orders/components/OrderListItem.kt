package com.example.mobile.orders.presentation.orders.components

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.ui.theme.MobileTheme
import java.text.SimpleDateFormat

@Composable
fun OrderListItem(
    modifier: Modifier = Modifier,
    orderEntity: OrderEntity
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .height(100.dp)
            .fillMaxWidth()
            .clickable {

            }
    ){
        Column {
            Text(
                text = orderEntity.code,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Row {
                val date = SimpleDateFormat("dd-MM-yyyy").parse(orderEntity.createdAt)
                Text(
                    text = date.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = orderEntity.status,
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
            orderEntity = fakeOrder
        )
    }
}