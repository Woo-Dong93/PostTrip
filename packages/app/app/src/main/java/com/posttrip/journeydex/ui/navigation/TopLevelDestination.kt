package com.posttrip.journeydex.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.posttrip.journeydex.feature.dex.navigation.dexNavigationRoute
import com.posttrip.journeydex.feature.home.navigation.homeNavigationRoute
import com.posttrip.journeydex.feature.map.navigation.mapNavigationRoute
import com.posttrip.journeydex.feature.reward.navigation.rewardNavigationRoute
import com.posttrip.journeydex.feature.home.R as home
import com.posttrip.journeydex.feature.dex.R as dex
import com.posttrip.journeydex.feature.map.R as map
import com.posttrip.journeydex.feature.reward.R as reward

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route : String
) {
    HOME(
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        iconTextId = home.string.home_title,
        titleTextId = home.string.home_title,
        route = homeNavigationRoute
    ),
    MAP(
        unselectedIcon = Icons.Outlined.LocationOn,
        selectedIcon = Icons.Filled.LocationOn,
        iconTextId = map.string.map_title,
        titleTextId = map.string.map_title,
        route = mapNavigationRoute
    ),
    DEX(
        unselectedIcon = Icons.Outlined.DateRange,
        selectedIcon = Icons.Filled.DateRange,
        iconTextId = dex.string.dex_title,
        titleTextId = dex.string.dex_title,
        route = dexNavigationRoute
    ),
    REWARD(
        unselectedIcon = Icons.Outlined.Star,
        selectedIcon = Icons.Filled.Star,
        iconTextId = reward.string.reward_title,
        titleTextId = reward.string.reward_title,
        route = rewardNavigationRoute
    ),
}