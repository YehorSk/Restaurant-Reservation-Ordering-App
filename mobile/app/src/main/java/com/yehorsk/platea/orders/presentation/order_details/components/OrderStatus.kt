package com.yehorsk.platea.orders.presentation.order_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.Utility.formatStringTime
import com.yehorsk.platea.core.utils.formatOrderDateTime
import com.yehorsk.platea.core.utils.Utility.statusToString
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun OrderStatus(
    modifier: Modifier = Modifier,
    status: String,
    date: String,
    code: String,
    startTime: String,
    endTime: String
){
    val context = LocalContext.current

    Card(
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp
                ),
                text = stringResource(R.string.order, code),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp
                ),
                text = stringResource(R.string.reservation_order_status, statusToString(status, context)),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp
                ),
                text = stringResource(R.string.reservation_order_date, formatOrderDateTime(date)),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 15.dp,
                    bottom = 15.dp
                ),
                text = stringResource(R.string.ready_between, "${formatStringTime(startTime)} - ${formatStringTime(endTime)}"),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun OrderStatusPreview(){
    MobileTheme {
        OrderStatus(
            status = "Pending",
            date = "05 May, 2024 10:40",
            code = "#KE1NB4",
            startTime = "18:00",
            endTime = "18:30"
        )
    }
}