package com.posttrip.journeydex.feature.reward.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val rewardNavigationRoute = "reward_route"

fun NavController.navigateToReward(navOptions: NavOptions? = null) {
    this.navigate(rewardNavigationRoute, navOptions)
}

fun NavGraphBuilder.rewardScreen() {
    composable(
        route = rewardNavigationRoute
    ) {
        Box{
            Text(text = rewardNavigationRoute)
        }
    }
}
