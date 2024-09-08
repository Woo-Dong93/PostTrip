package com.posttrip.journeydex.ui.navigation

import androidx.annotation.DrawableRes
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
import com.posttrip.journeydex.R
import com.posttrip.journeydex.feature.dex.navigation.dexNavigationRoute
import com.posttrip.journeydex.feature.home.navigation.homeNavigationRoute
import com.posttrip.journeydex.feature.map.navigation.mapNavigationRoute
import com.posttrip.journeydex.feature.reward.navigation.rewardNavigationRoute
import com.posttrip.journeydex.feature.home.R as home
import com.posttrip.journeydex.feature.dex.R as dex
import com.posttrip.journeydex.feature.map.R as map
import com.posttrip.journeydex.feature.reward.R as reward

enum class TopLevelDestination(
    @DrawableRes val icon: Int,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route : String
) {
    HOME(
        icon = R.drawable.ic_home,
        iconTextId = home.string.home_title,
        titleTextId = home.string.home_title,
        route = homeNavigationRoute
    ),
    MAP(
        icon = R.drawable.ic_map,
        iconTextId = map.string.map_title,
        titleTextId = map.string.map_title,
        route = mapNavigationRoute
    ),
    DEX(
        icon = R.drawable.ic_dex,
        iconTextId = dex.string.dex_title,
        titleTextId = dex.string.dex_title,
        route = dexNavigationRoute
    ),
    REWARD(
        icon = R.drawable.ic_user,
        iconTextId = reward.string.reward_title,
        titleTextId = reward.string.reward_title,
        route = rewardNavigationRoute
    ),
}
