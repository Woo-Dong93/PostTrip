package com.posttrip.journeydex.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.posttrip.journeydex.ui.navigation.JourneydexNavHost
import com.posttrip.journeydex.ui.navigation.TopLevelDestination

@Composable
fun JourneydexApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            JourneydexBottomBar(
                destinations = TopLevelDestination.entries,
                currentDestination = navController.currentBackStackEntryAsState().value?.destination,
                onNavigateToDestination = {
                    navController.navigate(it.route)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            JourneydexNavHost(
                navController = navController,
            )
        }
    }
}

@Composable
fun JourneydexBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
){
    JourneydexNavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            JourneydexNavigationBarItem(
                selected = selected,
                onClick = {
                    onNavigateToDestination(destination)
                },
                icon ={
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.titleTextId)
                    )
                }
            )
        }
    }
}

@Composable
fun JourneydexNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
){
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        tonalElevation = tonalElevation,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(56.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
            verticalAlignment = Alignment.CenterVertically
        )
    }
}

@Composable
fun RowScope.JourneydexNavigationBarItem(
    selected : Boolean,
    onClick : () -> Unit,
    label: @Composable () -> Unit,
    icon : @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
){
    NavigationBarItem(
        modifier = modifier,
        selected = selected,
        icon = if (selected) selectedIcon else icon,
        onClick = onClick,
        label = label
    )

}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

