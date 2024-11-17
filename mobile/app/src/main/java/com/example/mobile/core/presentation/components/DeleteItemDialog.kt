package com.example.mobile.core.presentation.components

import android.app.AlertDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.mobile.R
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun DeleteItemDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    @StringRes stringTitle: Int
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    AlertDialog(
        icon ={

        },
        title = {
            Text(
                text = stringResource(stringTitle),
                color = contentColor
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    text = stringResource(R.string.btn_yes),
                    color = contentColor
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.btn_no),
                    color = contentColor
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
fun DeleteItemDialogPreview(){
    MobileTheme {
        DeleteItemDialog(
            onConfirm = {},
            onDismiss = {},
            stringTitle = R.string.delete_confirmation
        )
    }
}