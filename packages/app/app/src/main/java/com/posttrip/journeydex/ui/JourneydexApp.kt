package com.posttrip.journeydex.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.posttrip.journeydex.MainViewModel
import com.posttrip.journeydex.MainViewModel.Companion.TypeFromLogin
import com.posttrip.journeydex.OnboardingScreen
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.feature.home.component.CourseDetailBottomSheet
import com.posttrip.journeydex.feature.map.navigation.navigateToMap
import com.posttrip.journeydex.ui.navigation.JourneydexNavHost
import com.posttrip.journeydex.ui.navigation.TopLevelDestination

@Composable
fun JourneydexApp(
    typeFromLogin: TypeFromLogin,
    loginData: LoginData,
    onTypeFormLoginChanged: (TypeFromLogin) -> Unit
) {
    val navController = rememberNavController()

    if (typeFromLogin == MainViewModel.Companion.TypeFromLogin.NeedsOnboarding) {
        OnboardingScreen(
            loginData = loginData,
            onSetOnboarding = {
                onTypeFormLoginChanged(TypeFromLogin.GoToHome)
            }
        )
    } else if (typeFromLogin == MainViewModel.Companion.TypeFromLogin.GoToHome) {
        Scaffold(
            bottomBar = {
                val target = navController.currentBackStackEntryAsState().value?.destination?.route
                if (TopLevelDestination.entries.any { target?.contains(it.route) == true })
                    JourneydexBottomBar(
                        destinations = TopLevelDestination.entries,
                        currentDestination = navController.currentBackStackEntryAsState().value?.destination,
                        onNavigateToDestination = {
                            val topLevelNavOptions = navOptions {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            if (it == TopLevelDestination.MAP) {
                                navController.navigateToMap(
                                    navOptions = topLevelNavOptions
                                )
                            } else {
                                navController.navigate(it.route, navOptions = topLevelNavOptions)
                            }

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
}

@Composable
fun JourneydexBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
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
                icon = {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = destination.icon),
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(id = destination.titleTextId),
                            fontSize = 11.sp
                        )
                    }

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
) {
    Surface(
        color = containerColor,
        shape = RectangleShape,
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

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun RowScope.JourneydexNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBarItem(
        modifier = modifier.weight(1f),
        selected = selected,
        icon = icon,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF222222),
            selectedTextColor = Color(0xFF222222),
            unselectedTextColor = Color(0xFFE2E2E2),
            unselectedIconColor = Color(0xFFE2E2E2),
            indicatorColor = Color.Transparent
        ),
        alwaysShowLabel = false
    )

}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

