package com.yehorsk.platea.core.presentation.components


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.utils.getUserBarItems

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    userRoles: UserRoles,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit,
){
    val screens = getUserBarItems(userRoles)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route}

    val windowSize = currentWindowSize()

    val navType = getNavigationSuiteType(windowSize.toDpSize())

    NavigationSuiteScaffold(
        modifier = modifier,
        layoutType = if(!bottomBarDestination) NavigationSuiteType.None else navType,
        navigationSuiteItems = {
            screens.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any{
                    it.route == item.route
                } == true
                item(
                    selected = isSelected,
                    onClick = {
                        if(!isSelected){
                            navController.navigate(item.route){
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.title)
                        )
                    },
                    label = {
                        AutoResizedText(
                            text = stringResource(item.title),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                )
            }
        },
        content = {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                }
            ) {
                content()
            }
        }
    )
}

/**
 * Per <a href="https://m3.material.io/components/navigation-drawer/guidelines">Material Design 3 guidelines</a>,
 * the selection of the appropriate navigation component should be contingent on the available
 * window size:
 * - Bottom Bar for compact window sizes (below 600dp)
 * - Navigation Rail for medium and expanded window sizes up to 1240dp (between 600dp and 1240dp)
 * - Navigation Drawer to window size above 1240dp
 */
fun getNavigationSuiteType(windowSize: DpSize): NavigationSuiteType {
    return if (windowSize.width > 1240.dp) {
        NavigationSuiteType.NavigationRail
    } else if (windowSize.width >= 600.dp) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteType.NavigationBar
    }
}

@Composable
private fun IntSize.toDpSize(): DpSize = with(LocalDensity.current) {
    DpSize(width.toDp(), height.toDp())
}
