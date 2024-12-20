package com.example.mobile.orders.presentation.create_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.core.utils.formatDateTime
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun TimeRoot(
    modifier: Modifier = Modifier,
    date: String,
    onTimeChanged: (String) -> Unit
){
    val items = listOf<String>("10:15","10:30","10:45","11:00","11:15","11:30","11:45","12:00","12:15")
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                ),
            text = formatDateTime(date),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        LazyRow(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items){ item ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape)
                        .width(80.dp)
                        .background(Color.White)
                        .clickable{
                            onTimeChanged(item)
                        },
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
}

@Preview
@Composable
fun TimeRootPreview(){
    MobileTheme {
        TimeRoot(
            date = "2024-12-20",
            onTimeChanged = {}
        )
    }
}