package com.posttrip.journeydex.feature.map.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.posttrip.journeydex.feature.map.MapScreen

const val mapNavigationRoute = "map_route"
internal const val contentIdArg = "contentId"

fun NavController.navigateToMap(contentId : String = "-1",navOptions: NavOptions? = null) {
    this.navigate(mapNavigationRoute + "/${contentId}", navOptions = navOptions)

}

fun NavGraphBuilder.mapScreen() {
    composable(
        route = "${mapNavigationRoute}/{$contentIdArg}",
        arguments = listOf(navArgument(contentIdArg) { type = NavType.StringType })
    ) {
        MapScreen()
    }
}
