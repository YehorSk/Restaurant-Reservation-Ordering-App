package com.example.mobile.core.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile.core.data.remote.UserRoles
import com.example.mobile.core.navigation.AdminNavGraph
import com.example.mobile.core.navigation.ChefNavGraph
import com.example.mobile.core.navigation.ClientNavGraph
import com.example.mobile.core.navigation.WaiterNavGraph


@Composable
fun MainScreenGraph(
    navController: NavHostController = rememberNavController(),
    userRoles: UserRoles,
    onLoggedOut:() -> Unit
){
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                userRoles = userRoles
            )
        }
    ) { contentPadding ->
        when(userRoles){
            UserRoles.ADMIN -> {
                AdminNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(contentPadding),
                    onLoggedOut = onLoggedOut
                )
            }
            UserRoles.CHEF -> {
                ChefNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(contentPadding),
                    onLoggedOut = onLoggedOut
                )
            }
            UserRoles.USER -> {
                ClientNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(contentPadding),
                    onLoggedOut = onLoggedOut
                )
            }
            UserRoles.WAITER -> {
                WaiterNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(contentPadding),
                    onLoggedOut = onLoggedOut
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    userRoles: UserRoles
){
    var screens = listOf<BottomBarScreen>()

    when(userRoles){
        UserRoles.USER -> {
            screens = listOf(
                BottomBarScreen.Home,
                BottomBarScreen.Cart,
                BottomBarScreen.Favorites,
                BottomBarScreen.Account
            )
        }
        UserRoles.ADMIN -> {
            screens = listOf(
                BottomBarScreen.Orders,
                BottomBarScreen.Reservations,
                BottomBarScreen.Account
            )
        }
        UserRoles.CHEF -> {
            screens = listOf(
                BottomBarScreen.Orders,
                BottomBarScreen.Account,
            )
        }
        UserRoles.WAITER -> {
            screens = listOf(
                BottomBarScreen.Home,
                BottomBarScreen.Cart,
                BottomBarScreen.Favorites,
                BottomBarScreen.Account
            )
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route}

    if(bottomBarDestination){
        NavigationBar{
            screens.forEach{ screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    NavigationBarItem(
        label = {
            Text(text = stringResource(screen.title))
        },
        icon = {
            if(isSelected){
                Icon(imageVector = screen.selectedIcon, contentDescription = stringResource(screen.title))
            }else{
                Icon(imageVector = screen.unselectedIcon, contentDescription = stringResource(screen.title))
            }
        },
        selected = isSelected,
        onClick ={
            if(!isSelected){
                navController.navigate(screen.route){
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        }
    )
}