package com.yehorsk.platea.core.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.Utility
import com.yehorsk.platea.core.utils.Utility.getDayScheduleTranslation

@Composable
fun SchedulePart(
    modifier: Modifier = Modifier,
    schedule: String
){
    val normalSchedule = Utility.getSchedule(schedule)
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 10.dp,
                bottom = 15.dp
            )
    ){
        Text(
            modifier = Modifier.padding(
                bottom = 10.dp
            ),
            text = stringResource(id = R.string.our_schedule),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        normalSchedule.forEach{ (day, schedule) ->
            val text = if(schedule.isOpen) getDayScheduleTranslation(context, day, schedule.hours) else getDayScheduleTranslation(context, day, stringResource(R.string.closed))
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}