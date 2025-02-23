package com.yehorsk.platea.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.yehorsk.platea.core.utils.Utility
import timber.log.Timber

@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    phone: String,
    code: String,
    onPhoneChanged: (String) -> Unit = {},
    onFullPhoneChanged: (String) -> Unit = {},
    onCodeChanged: (String) -> Unit = {},
    onPhoneValidated: (String) -> Unit = {},
    color: TextFieldColors = TextFieldDefaults.colors(
        disabledIndicatorColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background
    ),
    showText: Boolean,
    shape: Shape = TextFieldDefaults.shape
){

    val formattedCode = Utility.getCountryCodeFromPhoneNumber(code)

    var phoneValue by rememberSaveable { mutableStateOf(phone) }

    Timber.d("Code $code")

    val state = rememberKomposeCountryCodePickerState(
        showCountryCode = true,
        showCountryFlag = true,
        defaultCountryCode = formattedCode
    )

    LaunchedEffect(state.getFullPhoneNumber()) {
        onPhoneChanged(state.getPhoneNumberWithoutPrefix())
        onPhoneValidated(state.getFullPhoneNumber())
        onCodeChanged(state.getCountryPhoneCode())
        onFullPhoneChanged(state.getFullPhoneNumber())
        Timber.d("Phone : ${state.getFullPhoneNumber()}")
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        if(showText){
            Text(
                modifier = Modifier
                    .padding(
                        bottom = 15.dp
                    ),
                text = stringResource(R.string.what_is_your_number),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        KomposeCountryCodePicker(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            text = phoneValue,
            onValueChange = {
                phoneValue = it
            },
            colors = color,
            shape = shape
        )
    }
}

@PreviewLightDark
@Composable
fun ReservationPhoneInputPreview(){
    MobileTheme {
        PhoneInput(
            phone = "680000000",
            code = "+421",
            onPhoneChanged = {},
            showText = true
        )
    }
}