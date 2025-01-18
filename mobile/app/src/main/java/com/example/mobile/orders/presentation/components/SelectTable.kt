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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.mobile.R
import com.example.mobile.orders.data.remote.dto.TableDto
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun SelectTable(
    modifier: Modifier = Modifier,
    tables: List<TableDto>,
    selectedTable: TableDto,
    onTableChanged: (TableDto) -> Unit
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
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedTable.number.toString(),
                onValueChange = { onTableChanged },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    }
                    .clickable {
                        expanded = !expanded
                    },
                label = {
                    Text(
                        text = stringResource(R.string.table_number),
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
                            onTableChanged(table)
                            expanded = false
                        },
                        text = {
                            Text(
                                text = table.number.toString(),
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
    val tables = listOf<TableDto>(TableDto(number = "1", capacity = 1, createdAt = "", updatedAt = "", id = "1"))
    MobileTheme {
        SelectTable(
            tables = tables,
            selectedTable = TableDto(
                id = "1",
                createdAt = "2024-12-22 12:40:32",
                updatedAt = "2024-12-22 12:40:32",
                number = "1",
                capacity = 4
            ),
            onTableChanged = {}
        )
    }
}