package com.example.mobile.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onAction:() -> Unit,
    color: Color,
    @StringRes text: Int
){
    Button(
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonColors(
            contentColor = Color.White,
            containerColor = color,
            disabledContainerColor = color,
            disabledContentColor = color
        ),
        onClick = { onAction() }
    ){
        Text(
            text = stringResource(text),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}