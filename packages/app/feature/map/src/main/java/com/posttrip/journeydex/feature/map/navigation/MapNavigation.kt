package com.posttrip.journeydex.feature.map.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val mapNavigationRoute = "map_route"

fun NavController.navigateToMap(navOptions: NavOptions? = null) {
    this.navigate(mapNavigationRoute, navOptions)
}

fun NavGraphBuilder.mapScreen() {
    composable(
        route = mapNavigationRoute
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Text(text = mapNavigationRoute)
        }
    }
}
