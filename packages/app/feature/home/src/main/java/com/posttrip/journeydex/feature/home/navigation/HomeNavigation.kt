package com.posttrip.journeydex.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.posttrip.journeydex.core.data.model.mission.Mission
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.home.AllMissionScreen
import com.posttrip.journeydex.feature.home.FavoriteScreen
import com.posttrip.journeydex.feature.home.HomeScreen

const val homeNavigationRoute = "home_route"
const val allMissionNavigationRoute = "all_mission_route"

const val favoriteNavigationRoute = "favorite_route"


fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavController.navigateToAllMission(navOptions: NavOptions? = null) {
    this.navigate(allMissionNavigationRoute, navOptions)
}

fun NavController.navigateToFavorite(navOptions: NavOptions? = null) {
    this.navigate(favoriteNavigationRoute, navOptions)
}


fun NavGraphBuilder.homeScreen(

    onLoadingShow : (Boolean) -> Unit,
    onDetail : (Course) -> Unit,
    onNavigateMap : (String) -> Unit,
    onNavigateAllMission : () -> Unit,
) {
    composable(
        route = homeNavigationRoute
    ) {
        HomeScreen(
            onLoadingShow = onLoadingShow,
            onDetail = onDetail,
            onNavigateAllMission = onNavigateAllMission,
            onNavigateMap = onNavigateMap,
        )
    }
}


fun NavGraphBuilder.allMissionScreen(
    onMissionClick : (Mission) -> Unit,
    onBackClick: () -> Unit,
    ) {
    composable(
        route = allMissionNavigationRoute
    ) {
        AllMissionScreen(
            onMissionClick = onMissionClick,
            onBackClick = onBackClick
        )
    }
}

fun NavGraphBuilder.favoriteScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = favoriteNavigationRoute
    ) {
        FavoriteScreen(
            onBackClick = onBackClick
        )
    }
}
