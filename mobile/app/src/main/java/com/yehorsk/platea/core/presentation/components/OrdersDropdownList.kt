package com.yehorsk.platea.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.utils.toString

@Composable
fun OrdersDropdownList(
    modifier: Modifier = Modifier,
    filterOption: OrderFilter,
    @StringRes text: Int,
    onSelect: (OrderFilter) -> Unit
){
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        TextButton(onClick = { expanded = true }) {
            Text(text = stringResource(text, filterOption.toString(context)))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            OrderFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    onClick = {
                        onSelect(filter)
                        expanded = false
                    },
                    text = {
                        Text(text = filter.toString(context),)
                    }
                )
            }
        }
    }
}