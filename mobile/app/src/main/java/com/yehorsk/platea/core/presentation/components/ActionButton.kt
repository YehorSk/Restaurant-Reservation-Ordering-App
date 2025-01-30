package com.yehorsk.platea.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onAction:() -> Unit,
    @StringRes text: Int,
    enabled: Boolean
){
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onAction() },
        enabled = enabled
    ){
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = stringResource(text),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@PreviewLightDark
@Composable
fun ActionButtonPreview(){
    MobileTheme {
        ActionButton(
            onAction = {},
            text = R.string.confirm_order,
            enabled = true
        )
    }
}

@PreviewLightDark
@Composable
fun ActionButtonPreview2(){
    MobileTheme {
        ActionButton(
            onAction = {},
            text = R.string.confirm_order,
            enabled = false
        )
    }
}