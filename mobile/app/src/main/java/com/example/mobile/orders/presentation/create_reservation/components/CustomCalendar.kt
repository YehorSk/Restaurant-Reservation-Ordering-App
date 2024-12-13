package com.example.mobile.orders.presentation.create_reservation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun CustomCalendar(
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 20.dp,
                    bottom = 10.dp
                ),
            text = stringResource(R.string.date),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

    }
}

@Composable
fun CalendarContent(

){

}

@Preview
@Composable
fun CustomCalendarPreview(){
    MobileTheme {
        CustomCalendar()
    }
}