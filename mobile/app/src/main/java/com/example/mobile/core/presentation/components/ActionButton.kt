package com.example.mobile.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
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
import com.example.mobile.R
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onAction:() -> Unit,
    @StringRes text: Int,
    enabled: Boolean
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Button(
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = contentColor,
        ),
        onClick = { onAction() },
        enabled = enabled
    ){
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = stringResource(text),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
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