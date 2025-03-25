package com.yehorsk.platea.orders.presentation.create_order.components

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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.Utility
import com.yehorsk.platea.core.utils.Utility.generateTimeSlots
import com.yehorsk.platea.core.utils.Utility.getDayName
import com.yehorsk.platea.core.utils.Utility.getDayTranslation
import com.yehorsk.platea.ui.theme.MobileThemePreview
import timber.log.Timber
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTimeModal(
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit,
    onTimeSelect: (String, String) -> Unit,
    onDateSelect: (String) -> Unit,
    selectedTime: String,
    selectedDate: String,
    schedule: String
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
                selectedTime = selectedTime,
                selectedDate = selectedDate,
                onDateSelect = onDateSelect,
                schedule = schedule
            )
        }
    )
}

@Composable
fun ChooseTimeModalContent(
    modifier: Modifier = Modifier,
    onDismiss:()->Unit,
    onTimeSelect: (String, String) -> Unit,
    selectedTime: String,
    onDateSelect: (String) -> Unit,
    selectedDate: String,
    schedule: String
){

    val normalSchedule = Utility.getSchedule(schedule)
    Timber.d("Schedule: $normalSchedule")

    var dayName = getDayName(selectedDate)

    val (startTime, endTime) = normalSchedule[dayName]!!.hours.split("-")
    val startHour = startTime.split(":")[0].toInt()
    val endHour = endTime.split(":")[0].toInt()

    val timeSlots = remember(selectedDate) {
        generateTimeSlots(
            intervalMinutes = 15,
            startTimeSchedule = startHour,
            endTimeSchedule = endHour,
            date = selectedDate
        )
    }
    val daySlots = listOf<DayItem>(
        DayItem(
            name = R.string.today,
            date = LocalDate.now().toString(),
            isOpen = normalSchedule[getDayName(LocalDate.now().toString())]!!.isOpen
        ),
        DayItem(
            name = R.string.tomorrow,
            date = LocalDate.now().plusDays(1).toString(),
            isOpen = normalSchedule[getDayName(LocalDate.now().plusDays(1).toString()).lowercase().replaceFirstChar { it.uppercaseChar() }]!!.isOpen
        ),
        DayItem(
            name = getDayTranslation(getDayName(LocalDate.now().plusDays(2).toString())),
            date = LocalDate.now().plusDays(2).toString(),
            isOpen = normalSchedule[getDayName(LocalDate.now().plusDays(2).toString()).lowercase().replaceFirstChar { it.uppercaseChar() }]!!.isOpen
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
                        onClick = onDismiss,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.tertiary)
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
        Text(
            text = stringResource(id = R.string.select_time),
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 15.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyColumn(modifier = Modifier
                .wrapContentHeight()
                .weight(1f)) {
                items(daySlots){ item ->
                    if(item.isOpen){
                        DaySlotItem(
                            dayItem = item,
                            onDaySelect = {date -> onDateSelect(date)},
                            selectedDay = selectedDate
                        )
                    }
                }
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .weight(1f)) {
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
            containerColor = if (selectedTime != "${timeItem.startTime} - ${timeItem.endTime}")
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.tertiary
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

@Composable
fun DaySlotItem(
    dayItem: DayItem,
    onDaySelect: (String) -> Unit,
    selectedDay: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onDaySelect(dayItem.date)
            }
            .padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 20.dp,
                end = 20.dp
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (selectedDay != dayItem.date)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.tertiary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                text = stringResource(dayItem.name),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = dayItem.date,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class TimeItem(
    val startTime: String,
    val endTime: String,
)

data class DayItem(
    @StringRes val name: Int,
    val date: String,
    val isOpen: Boolean
)

@Preview
@Composable
fun ChooseTimeModalContentPreview(){
    MobileThemePreview {
        ChooseTimeModalContent(
            modifier = Modifier,
            onDismiss = {},
            onTimeSelect = { one, two -> },
            selectedTime = "",
            selectedDate = "24-03-2025",
            onDateSelect = { two -> },
            schedule = ""
        )
    }
}

