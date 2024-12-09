package com.example.mobile.orders.presentation.create_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun PartySize(
    modifier: Modifier = Modifier
){
    val items = (1..10).toList()
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items){ item ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .size(50.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier.padding(10.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    text = item.toString()
                )
            }
        }
    }
}

@Preview
@Composable
fun PartySizePreview(){
    MobileTheme {
        PartySize()
    }
}