package com.yehorsk.platea.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.notifications.data.db.model.NotificationEntity

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    notification: NotificationEntity,
    onClick: (NotificationEntity) -> Unit
){
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .clickable {
                onClick(notification)
            },
    ) {
        Text(
            text = notification.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
        Text(
            text = notification.body,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
    }
}