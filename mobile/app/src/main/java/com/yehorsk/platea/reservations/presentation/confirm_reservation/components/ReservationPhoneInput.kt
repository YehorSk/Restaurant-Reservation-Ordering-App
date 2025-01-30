package com.yehorsk.platea.reservations.presentation.confirm_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState

@Composable
fun ReservationPhoneInput(
    phone: String,
    onPhoneChanged: (String) -> Unit
){

    var phoneValue by rememberSaveable { mutableStateOf("") }
    val state = rememberKomposeCountryCodePickerState(
        showCountryCode = true,
        showCountryFlag = true,
        defaultCountryCode = "SK",
    )

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
            text = stringResource(R.string.what_is_your_number),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        KomposeCountryCodePicker(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            text = phoneValue,
            onValueChange = {
                phoneValue = it
                onPhoneChanged(state.getFullPhoneNumber())
            },
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
fun ReservationPhoneInputPreview(){
    MobileTheme {
        ReservationPhoneInput(
            phone = "680000000",
            onPhoneChanged = {}
        )
    }
}