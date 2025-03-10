package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.Pivot
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun OrderItemList(
    items: List<OrderMenuItemDto>
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        for (item in items) {
            OrderListItem(item) {
            }
        }
    }
}

@Preview
@Composable
fun OrderItemListPreview(){
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
                orderId = 10,
                menuItemId = 1,
                quantity = 2,
                price = 17.98,
                createdAt = "2024-01-01T10:00:00Z",
                updatedAt = "2024-01-01T10:05:00Z"
            )
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
                orderId = 15,
                menuItemId = 2,
                quantity = 1,
                price = 12.99,
                createdAt = "2024-01-03T15:30:00Z",
                updatedAt = "2024-01-03T15:35:00Z"
            ),
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
                orderId = 20,
                menuItemId = 3,
                quantity = 1,
                price = 7.50,
                createdAt = "2024-01-05T09:10:00Z",
                updatedAt = "2024-01-05T09:15:00Z"
            ),
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
                orderId = 25,
                menuItemId = 4,
                quantity = 1,
                price = 10.50,
                createdAt = "2024-01-06T14:10:00Z",
                updatedAt = "2024-01-06T14:20:00Z"
            ),
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
                orderId = 30,
                menuItemId = 5,
                quantity = 3,
                price = 20.97,
                createdAt = "2024-01-08T08:30:00Z",
                updatedAt = "2024-01-08T08:35:00Z"
            ),
        )
    )
    MobileTheme {
        OrderItemList(
            items = fakeItems
        )
    }
}