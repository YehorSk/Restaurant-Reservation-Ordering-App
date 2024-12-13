package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile.ui.theme.MobileTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@Composable
fun SelectTable(
    modifier: Modifier = Modifier,
    tables: List<Int>,
    selectedTable: Int,
    onTableNumberChanged: (Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Card() {
        Column(
            modifier = modifier
                .background(Color.White)
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedTable.toString(),
                onValueChange = { onTableNumberChanged },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
                    .clickable{
                        expanded = !expanded
                    },
                label = {
                    Text(
                        text = "Number"
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable{
                                expanded = !expanded
                            }
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded=false },
                modifier = Modifier
                    .width(with(LocalDensity.current){textFieldSize.width.toDp()})
            ) {
                tables.forEach{ table ->
                    DropdownMenuItem(
                        onClick = {
                            onTableNumberChanged(table)
                            expanded = false
                        },
                        text = {
                            Text(
                                text = table.toString()
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectTablePreview(){
    val tables = listOf<Int>(1,2,3,4,5,6,7,8,9,10)
    val selected = 1
    MobileTheme {
        SelectTable(
            tables = tables,
            selectedTable = selected,
            onTableNumberChanged = {}
        )
    }
}