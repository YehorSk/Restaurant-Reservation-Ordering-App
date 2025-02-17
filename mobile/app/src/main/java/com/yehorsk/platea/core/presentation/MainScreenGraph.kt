package com.yehorsk.platea.core.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.navigation.AdminNavGraph
import com.yehorsk.platea.core.navigation.ChefNavGraph
import com.yehorsk.platea.core.navigation.ClientNavGraph
import com.yehorsk.platea.core.navigation.WaiterNavGraph
import com.yehorsk.platea.core.utils.snackbar.ObserveAsEvents
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.toString
import kotlinx.coroutines.launch


@Composable
fun MainScreenGraph(
    navController: NavHostController = rememberNavController(),
    userRoles: UserRoles,
    onLoggedOut:() -> Unit
){
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
        scope.launch{
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = if(event.error != null) event.error.toString(context) else event.message!!,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            if(result == SnackbarResult.ActionPerformed){
                event.action?.action?.invoke()
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                userRoles = userRoles
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
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
            Text(
                text = stringResource(screen.title),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )
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