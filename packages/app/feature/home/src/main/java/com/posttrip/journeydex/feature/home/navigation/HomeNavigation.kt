package com.posttrip.journeydex.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.home.HomeScreen

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onDetail : (Course) -> Unit,
    onNavigateMap : (String) -> Unit,
) {
    composable(
        route = homeNavigationRoute
    ) {
        HomeScreen(
            onDetail = onDetail,
            onNavigateMap = onNavigateMap,
        )
    }
}
