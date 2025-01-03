package com.example.mobile.menu.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteItemDialog(
    modifier: Modifier = Modifier,
    onDissmiss: () -> Unit,
    onConfirm: () -> Unit,
){
    BasicAlertDialog(
        content = {
            Surface(
                modifier = Modifier.wrapContentWidth(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text ="This area typically contains the supportive text which presents the details regarding the Dialog's purpose.")
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = onDissmiss,
                        modifier = Modifier. align(Alignment.End)
                    ) {
                        Text("Confirm")
                    }
                }
            }
        },
        onDismissRequest = onDissmiss,
    )
}