package com.posttrip.journeydex.feature.dex.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val dexNavigationRoute = "dex_route"

fun NavController.navigateToDex(navOptions: NavOptions? = null) {
    this.navigate(dexNavigationRoute, navOptions)
}

fun NavGraphBuilder.dexScreen() {
    composable(
        route = dexNavigationRoute
    ) {
        Box{
            Text(text = dexNavigationRoute)
        }
    }
}
