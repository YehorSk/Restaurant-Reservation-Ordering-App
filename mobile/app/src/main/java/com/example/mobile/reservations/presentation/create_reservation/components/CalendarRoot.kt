package com.example.mobile.reservations.presentation.create_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile.ui.theme.MobileTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.core.utils.formatMonth
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch

@Composable
fun CalendarRoot(
    onUpdateSelectedDate: (String) -> Unit
){

    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }

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
            .padding(20.dp)
    ) {
//        Text(
//            modifier = Modifier.padding(bottom = 20.dp),
//            text = formatDateTime(selectedDate.toString())
//        )
        Text(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                ),
            text = stringResource(R.string.date),
            color = contentColor,
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
                    tint = contentColor,
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                text = formatMonth(state.lastVisibleMonth.yearMonth.toString()),
                color = contentColor,
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
                    tint = contentColor,
                    contentDescription = ""
                )
            }
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day,
                    isSelected = selectedDate == day.date,
                    contentColor = contentColor,
                    onClick = { day ->
                        selectedDate = if (selectedDate == day.date) selectedDate else day.date
                        onUpdateSelectedDate(selectedDate.toString())
                    }
                )
            },
            monthHeader = {
                DaysOfWeekTitle(
                    daysOfWeek = daysOfWeek(),
                    contentColor = contentColor
                )
            }
        )
    }


}

@Composable
fun DaysOfWeekTitle(
    daysOfWeek: List<DayOfWeek>,
    contentColor: Color
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
                color = contentColor,
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
    contentColor: Color
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