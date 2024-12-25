package com.example.mobile.orders.presentation.reservations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.core.utils.formatOrderDateTime
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.orders.data.db.model.ReservationEntity

@Composable
fun ReservationItem(
    modifier: Modifier = Modifier,
    reservationEntity: ReservationEntity
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .height(120.dp)
            .fillMaxWidth()
            .clickable {

            }
    ){
        Column {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = "#${reservationEntity.code}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                text = "Table: ${reservationEntity.tableId}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            ) {
                Text(
                    text = formatOrderDateTime(reservationEntity.createdAt),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                )
                Text(
                    text = " ${reservationEntity.status}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                )
            }
        }
    }
}