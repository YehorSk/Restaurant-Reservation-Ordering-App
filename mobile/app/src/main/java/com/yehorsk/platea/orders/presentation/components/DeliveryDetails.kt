package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.PhoneInput
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun DeliveryDetails(
    modifier: Modifier = Modifier,
    address: String,
    instructions: String,
    phone: String,
    code: String,
    onPhoneChanged: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onInstructionsChange: (String) -> Unit,
    onPhoneValidated: (Boolean) -> Unit = {}
) {

    var phoneValue by rememberSaveable { mutableStateOf(phone) }
    var codeValue by rememberSaveable { mutableStateOf(code) }

    Card(
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            PhoneInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                phone = phoneValue,
                code = codeValue,
                showText = false,
                onPhoneChanged = { it -> onPhoneChanged(it) },
                color = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = MaterialTheme.shapes.small
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                value = address,
                onValueChange = {it -> onAddressChange(it)},
                placeholder = {
                    Text(
                        text = stringResource(R.string.delivery_address),
                    )
                },
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = MaterialTheme.shapes.small
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                value = instructions,
                onValueChange = {it -> onInstructionsChange(it)},
                placeholder = {
                    Text(
                        text = "*"+stringResource(R.string.instructions_for_the_courier),
                    )
                },
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = MaterialTheme.shapes.small
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        top = 15.dp,
                        bottom = 10.dp
                    ),
                text = stringResource(R.string.required_fields),
                fontSize = 16.sp,
                color = Color.LightGray
            )
        }
    }
}

@PreviewLightDark
@Composable
fun OrderAddressPreview(){
    MobileTheme {
        DeliveryDetails(
            address = "Test Address",
            instructions = "Hello World",
            onAddressChange = {},
            onInstructionsChange = {},
            code = "",
            phone = "",
            onPhoneChanged = {},
            onPhoneValidated = {}
        )
    }
}