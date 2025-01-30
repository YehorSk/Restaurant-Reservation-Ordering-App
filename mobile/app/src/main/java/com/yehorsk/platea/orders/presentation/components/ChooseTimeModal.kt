package com.yehorsk.platea.orders.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTimeModal(
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit,
    onOrderTypeSelect: (Int,String)-> Unit,
    onTimeSelect: (String, String) -> Unit,
    selectedTime: String,
    selected: Int
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        content = {
            ChooseTimeModalContent(
                onDismiss = onDismiss,
                onTimeSelect = onTimeSelect,
                onOrderTypeSelect = onOrderTypeSelect,
                selectedTime = selectedTime,
                selectedOrderType = selected
            )
        }
    )
}

@Composable
fun ChooseTimeModalContent(
    modifier: Modifier = Modifier,
    onDismiss:()->Unit,
    onOrderTypeSelect: (Int,String)-> Unit,
    onTimeSelect: (String, String) -> Unit,
    selectedTime: String,
    selectedOrderType: Int
){
    val timeSlots = remember { generateTimeSlots() }

    val list = listOf(
        TabItem(
            title = R.string.pickup_option,
            orderType = 0
        ),
        TabItem(
            title = R.string.delivery_option,
            orderType = 1
        )
    )

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(40.dp),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(timeSlots){ item ->
                TimeSlotItem(
                    timeItem = item,
                    onTimeSelect = { start, end -> onTimeSelect(start, end) },
                    selectedTime = selectedTime
                )
            }
        }
    }
}

@Composable
fun TimeSlotItem(
    timeItem: TimeItem,
    onTimeSelect: (String, String) -> Unit,
    selectedTime: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTimeSelect(timeItem.startTime, timeItem.endTime)
            }
            .padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 20.dp,
                end = 20.dp
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (!selectedTime.equals("${timeItem.startTime} - ${timeItem.endTime}"))
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.surfaceTint
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "${timeItem.startTime} - ${timeItem.endTime}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun generateTimeSlots(intervalMinutes: Int = 15): List<TimeItem> {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    val now = LocalTime.now()

    val roundedNow = now.withMinute((now.minute / intervalMinutes) * intervalMinutes).withSecond(0).withNano(0)

    val endTime = LocalTime.of(20, 0)
    val timeSlots = mutableListOf<TimeItem>()

    var currentStart = roundedNow
    while (currentStart.plusMinutes(30).isBefore(endTime) || currentStart.plusMinutes(30) == endTime) {
        val currentEnd = currentStart.plusMinutes(30)
        timeSlots.add(TimeItem(currentStart.format(formatter), currentEnd.format(formatter)))
        currentStart = currentStart.plusMinutes(intervalMinutes.toLong())
    }

    return timeSlots
}

data class TimeItem(
    val startTime: String,
    val endTime: String,
)

data class TabItem(
    @StringRes val title: Int,
    val orderType: Int
)

