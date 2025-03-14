package com.yehorsk.platea.reservations.presentation.create_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.formatMonth
import com.yehorsk.platea.ui.theme.MobileTheme
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarRoot(
    onUpdateSelectedDate: (String) -> Unit
){

    val coroutineScope = rememberCoroutineScope()
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth }
    val endMonth = remember { currentMonth.plusMonths(12) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp
            )
    ) {
        Text(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                ),
            text = stringResource(R.string.date),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val previousMonth = state.firstVisibleMonth.yearMonth.previousMonth
                        if (previousMonth >= startMonth) {
                            state.scrollToMonth(previousMonth)
                        }
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                text = formatMonth(state.lastVisibleMonth.yearMonth.toString()),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val nextMonth = state.firstVisibleMonth.yearMonth.nextMonth
                        if (nextMonth <= endMonth) {
                            state.scrollToMonth(nextMonth)
                        }
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = ""
                )
            }
        }
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day,
                    isSelected = selectedDate == day.date,
                    onClick = { day ->
                        selectedDate = if (selectedDate == day.date) selectedDate else day.date
                        onUpdateSelectedDate(selectedDate.toString())
                    }
                )
            },
            monthHeader = {
                DaysOfWeekTitle(
                    daysOfWeek = daysOfWeek(),
                )
            }
        )
    }


}

@Composable
fun DaysOfWeekTitle(
    daysOfWeek: List<DayOfWeek>
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 10.dp
            )
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit,
) {
    val isPastDate = day.date.isBefore(LocalDate.now())
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = when{
                day.position != DayPosition.MonthDate -> Color.Transparent
                isSelected -> MaterialTheme.colorScheme.primary
                isPastDate -> Color.LightGray
                else -> Color.White
            })
            .border(
                width = 2.dp,
                color = when{
                    day.position != DayPosition.MonthDate -> Color.Transparent
                    isSelected -> Color.Transparent
                    isPastDate -> Color.Transparent
                    else -> Color.LightGray
                },
                shape = RoundedCornerShape(10.dp))
            .clickable(
                enabled = day.position == DayPosition.MonthDate && !isPastDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                day.position != DayPosition.MonthDate -> Color.Transparent
                isPastDate -> Color.Gray
                isSelected -> Color.White
                else -> Color.Black
            }
        )
    }
}

@Preview
@Composable
fun CalendarPreview(){
    MobileTheme {
        CalendarRoot(
            onUpdateSelectedDate = {}
        )
    }
}