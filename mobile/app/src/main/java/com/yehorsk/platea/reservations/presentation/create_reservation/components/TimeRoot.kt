package com.yehorsk.platea.reservations.presentation.create_reservation.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.core.utils.formatDateTime
import com.yehorsk.platea.core.utils.formatTime
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun TimeRoot(
    modifier: Modifier = Modifier,
    selectedSlot: Int,
    date: String,
    slots: List<TimeSlotDto>,
    onTimeChanged: (Int, String) -> Unit
){

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
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
            items(slots.filter { it.availableTables > 0 }){ item ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .border(
                            width = 2.dp,
                            color = if (item.id == selectedSlot) MaterialTheme.colorScheme.primary else Color.LightGray,
                            shape = CircleShape)
                        .width(80.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .clickable{
                            onTimeChanged(item.id, item.startTime)
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier.padding(10.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        text = formatTime(item.startTime.toString())
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TimeRootPreview(){
    val timeSlots = listOf(
        TimeSlotDto(id = 1, startTime = "08:00:00", endTime = "09:00:00", availableTables = 0),
        TimeSlotDto(id = 2, startTime = "09:00:00", endTime = "10:00:00", availableTables = 8),
        TimeSlotDto(id = 3, startTime = "10:00:00", endTime = "11:00:00", availableTables = 8),
        TimeSlotDto(id = 4, startTime = "11:00:00", endTime = "12:00:00", availableTables = 8),
        TimeSlotDto(id = 5, startTime = "12:00:00", endTime = "13:00:00", availableTables = 8),
        TimeSlotDto(id = 6, startTime = "13:00:00", endTime = "14:00:00", availableTables = 8),
        TimeSlotDto(id = 7, startTime = "14:00:00", endTime = "15:00:00", availableTables = 8),
        TimeSlotDto(id = 8, startTime = "15:00:00", endTime = "16:00:00", availableTables = 8),
        TimeSlotDto(id = 9, startTime = "16:00:00", endTime = "17:00:00", availableTables = 8),
        TimeSlotDto(id = 10, startTime = "17:00:00", endTime = "18:00:00", availableTables = 8)
    )
    MobileTheme {
        TimeRoot(
            date = "2024-12-20",
            slots = timeSlots,
            onTimeChanged = {id, time ->},
            selectedSlot = 0
        )
    }
}