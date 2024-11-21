package com.example.mobile.orders.presentation.create_order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.Pivot
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.orders.presentation.components.OrderAddMore
import com.example.mobile.orders.presentation.components.OrderItemList
import com.example.mobile.orders.presentation.components.OrderMap
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.orders.presentation.components.OrderOptions
import com.example.mobile.orders.presentation.components.TotalPrice

@Composable
fun CreateOrderScreen(
    viewModel: CreateOrderViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
){

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected = viewModel.isNetwork.collectAsStateWithLifecycle(false)

    val fakeItems = listOf(
        OrderMenuItemDto(
            id = 1,
            createdAt = "2024-01-01T10:00:00Z",
            updatedAt = "2024-01-02T12:00:00Z",
            menuId = 101,
            name = "Classic Cheeseburger",
            shortDescription = "Juicy beef patty with melted cheese",
            longDescription = "A classic cheeseburger made with 100% beef patty, cheddar cheese, lettuce, tomato, and our signature sauce.",
            recipe = "Beef patty, cheddar, lettuce, tomato, sauce, sesame bun",
            picture = "https://example.com/images/cheeseburger.jpg",
            price = 8.99,
            pivot = Pivot(
                id = 1,
                userId = 10,
                menuItemId = 1,
                quantity = 2,
                price = 17.98,
                createdAt = "2024-01-01T10:00:00Z",
                updatedAt = "2024-01-01T10:05:00Z"
            ),
            isFavorite = true
        ),
        OrderMenuItemDto(
            id = 2,
            createdAt = "2024-01-03T15:00:00Z",
            updatedAt = "2024-01-04T16:00:00Z",
            menuId = 102,
            name = "Margherita Pizza",
            shortDescription = "Classic pizza with fresh basil",
            longDescription = "A traditional Italian pizza with fresh mozzarella, tomatoes, and basil leaves.",
            recipe = "Pizza dough, mozzarella, tomato sauce, basil leaves",
            picture = "https://example.com/images/margherita_pizza.jpg",
            price = 12.99,
            pivot = Pivot(
                id = 2,
                userId = 15,
                menuItemId = 2,
                quantity = 1,
                price = 12.99,
                createdAt = "2024-01-03T15:30:00Z",
                updatedAt = "2024-01-03T15:35:00Z"
            ),
            isFavorite = false
        ),
        OrderMenuItemDto(
            id = 3,
            createdAt = "2024-01-05T09:00:00Z",
            updatedAt = "2024-01-05T10:00:00Z",
            menuId = 103,
            name = "Caesar Salad",
            shortDescription = "Fresh lettuce with Caesar dressing",
            longDescription = "A refreshing Caesar salad made with romaine lettuce, croutons, Parmesan cheese, and a classic Caesar dressing.",
            recipe = "Romaine lettuce, croutons, Parmesan, Caesar dressing",
            picture = "https://example.com/images/caesar_salad.jpg",
            price = 7.50,
            pivot = Pivot(
                id = 3,
                userId = 20,
                menuItemId = 3,
                quantity = 1,
                price = 7.50,
                createdAt = "2024-01-05T09:10:00Z",
                updatedAt = "2024-01-05T09:15:00Z"
            ),
            isFavorite = true
        ),
        OrderMenuItemDto(
            id = 4,
            createdAt = "2024-01-06T14:00:00Z",
            updatedAt = "2024-01-07T15:00:00Z",
            menuId = 104,
            name = "Spaghetti Carbonara",
            shortDescription = "Pasta with creamy sauce",
            longDescription = "Classic Italian pasta dish with creamy sauce, pancetta, Parmesan, and black pepper.",
            recipe = "Spaghetti, eggs, pancetta, Parmesan, pepper",
            picture = "https://example.com/images/spaghetti_carbonara.jpg",
            price = 10.50,
            pivot = Pivot(
                id = 4,
                userId = 25,
                menuItemId = 4,
                quantity = 1,
                price = 10.50,
                createdAt = "2024-01-06T14:10:00Z",
                updatedAt = "2024-01-06T14:20:00Z"
            ),
            isFavorite = false
        ),
        OrderMenuItemDto(
            id = 5,
            createdAt = "2024-01-08T08:00:00Z",
            updatedAt = "2024-01-08T09:00:00Z",
            menuId = 105,
            name = "Chocolate Lava Cake",
            shortDescription = "Warm chocolate dessert",
            longDescription = "A decadent dessert with a gooey chocolate center, served warm with vanilla ice cream.",
            recipe = "Chocolate, eggs, butter, sugar, flour, ice cream",
            picture = "https://example.com/images/lava_cake.jpg",
            price = 6.99,
            pivot = Pivot(
                id = 5,
                userId = 30,
                menuItemId = 5,
                quantity = 3,
                price = 20.97,
                createdAt = "2024-01-08T08:30:00Z",
                updatedAt = "2024-01-08T08:35:00Z"
            ),
            isFavorite = true
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(
            onGoBack = onGoToCart,
            title = R.string.complete_order_navbar_title
        )
        OrderItemList(items = fakeItems)
        HorizontalDivider()
        OrderAddMore(
            onGoToCart = onGoToCart,
            onGoToMenu = onGoToMenu
        )
        HorizontalDivider()
        OrderSpecialRequest(
            request = uiState.value.request,
            onRequestChange = {request -> viewModel.updateRequest(request)}
        )
        Spacer(modifier = Modifier.height(10.dp))
        OrderOptions(
            selected = uiState.value.orderType,
            onSelectedChange = { type,text -> viewModel.updateOrderType(type,text) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TotalPrice()
        Spacer(modifier = Modifier.height(10.dp))
        if (uiState.value.orderType == 1 && isConnected.value){
            OrderMap()
        }
        Button(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 10.dp
                )
                .fillMaxWidth(),
            onClick = {}
        ) {
            Text(
                text = "Order ${uiState.value.orderText}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun OrderSpecialRequest(
    request: String,
    onRequestChange: (String) -> Unit
){
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        value = request,
        placeholder = {
            Text(
                text = stringResource(R.string.notes_for_order)
            )
        },
        onValueChange = {
            onRequestChange(it)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}


@Preview
@Composable
fun CreateOrderScreenPreview(
    modifier: Modifier = Modifier
){
    MobileTheme {
        CreateOrderScreen(
            onGoToMenu = {},
            onGoToCart = {}
        )
    }
}