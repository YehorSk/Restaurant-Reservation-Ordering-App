package com.example.mobile.orders.presentation.order_details.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.R
import com.example.mobile.core.presentation.components.ActionButton

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onRepeatOrder:() -> Unit,
    onCancelOrder:() -> Unit,
    allowCancel: Boolean
){
    Card(
    ) {
        Column(
            modifier = modifier
                .background(Color.White)
                .fillMaxWidth()
        ) {
            ActionButton(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 5.dp
                    ),
                onAction = onRepeatOrder,
                color = MaterialTheme.colorScheme.primary,
                text = R.string.repeat_order
            )
            if(allowCancel){
                ActionButton(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 5.dp,
                            bottom = 20.dp
                        ),
                    onAction = onCancelOrder,
                    color = MaterialTheme.colorScheme.error,
                    text = R.string.cancel_order
                )
            }
        }
    }
}

@Preview
@Composable
fun ActionButtonsPreview(){
    MobileTheme {
        ActionButtons(
            onRepeatOrder = {},
            onCancelOrder = {},
            allowCancel = true
        )
    }
}