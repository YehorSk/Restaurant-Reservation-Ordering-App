package com.example.mobile.reservations.presentation.confirm_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun ReservationSpecialRequest(
    request: String,
    onRequestChange: (String) -> Unit
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(
                    bottom = 15.dp
                ),
            text = stringResource(R.string.reservation_special_request),
            color = contentColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.type_request_here),
                    color = contentColor
                )
            },
            maxLines = 2,
            minLines = 2,
            value = request,
            onValueChange = { onRequestChange(it) },
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            )
        )
    }
}

@PreviewLightDark
@Composable
fun ReservationSpecialRequestPreview(){
    MobileTheme {
        ReservationSpecialRequest(
            request = "Lorem ipsum",
            onRequestChange = {}
        )
    }
}