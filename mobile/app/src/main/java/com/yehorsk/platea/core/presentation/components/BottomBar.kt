package com.yehorsk.platea.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.presentation.BottomBarScreen
import com.yehorsk.platea.core.utils.getUserBarItems
import timber.log.Timber


@Composable
fun BottomBar(
    navController: NavHostController,
    userRoles: UserRoles,
    cartItems: Int
){
    val screens = getUserBarItems(userRoles)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.map { it.screen::class }.any{ route ->
        currentDestination?.hierarchy?.any{
            it.hasRoute(route)
        } == true
    }
    Timber.d("bottomBarDestination $bottomBarDestination")

    if(bottomBarDestination){
        NavigationBar{
            screens.forEach{ screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                    cartItems = cartItems
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    cartItems: Int
){
    val isSelected = currentDestination?.hierarchy?.any{ it.route == screen.screen::class.qualifiedName} == true

    val isCartSelected = screen.screen == BottomBarScreen.Cart.screen
    NavigationBarItem(
        label = {
            AutoResizedText(
                text = stringResource(screen.title),
                style = MaterialTheme.typography.labelSmall
            )
        },
        icon = {
            if(cartItems > 0 && isCartSelected){
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Color.White
                        ){
                            Text(
                                text = cartItems.toString(),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = stringResource(screen.title)
                    )
                }
            }else{
                Icon(
                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                    contentDescription = stringResource(screen.title)
                )
            }
        },
        selected = isSelected,
        onClick ={
            if(!isSelected){
                navController.navigate(screen.screen){
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        }
    )
}