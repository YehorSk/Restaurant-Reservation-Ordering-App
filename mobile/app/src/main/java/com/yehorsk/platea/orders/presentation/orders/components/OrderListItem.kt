package com.yehorsk.platea.orders.presentation.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.Utility.getOrderColorIndicator
import com.yehorsk.platea.core.utils.Utility.statusToString
import com.yehorsk.platea.core.utils.formatOrderDateTime
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.ui.theme.MobileTheme
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun OrderListItem(
    modifier: Modifier = Modifier,
    orderEntity: OrderEntity,
    onGoToOrderDetails: (Int) -> Unit,
    showStatus: Boolean = false
){
    val context = LocalContext.current
    val statusColor = getOrderColorIndicator(
        status = orderEntity.status,
        date = orderEntity.date.toString(),
        endTime = orderEntity.endTime
    )
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .clickable {
                onGoToOrderDetails(orderEntity.id.toInt())
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = orderEntity.code,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            if(showStatus && (LocalDate.parse(orderEntity.date) == LocalDate.now() && LocalTime.parse(orderEntity.endTime).isAfter(LocalTime.now()))){
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    text = stringResource(R.string.complete_by, orderEntity.endTime),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
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
                    text = " ${statusToString(orderEntity.status, context)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        if(showStatus){
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                    .fillMaxHeight()
                    .width(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(statusColor)
            )
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
        code = "F6JKN1",
        startTime = "",
        endTime = "",
        completedAt = "2024-12-01T12:34:56",
        phone = "",
        date = "2024-12-01"
    )
    MobileTheme {
        OrderListItem(
            orderEntity = fakeOrder,
            onGoToOrderDetails = {}
        )
    }
}