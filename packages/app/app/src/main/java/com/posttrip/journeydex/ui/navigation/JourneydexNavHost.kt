package com.posttrip.journeydex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.feature.dex.navigation.dexScreen
import com.posttrip.journeydex.feature.home.navigation.homeNavigationRoute
import com.posttrip.journeydex.feature.home.navigation.homeScreen
import com.posttrip.journeydex.feature.map.navigation.mapScreen
import com.posttrip.journeydex.feature.map.navigation.navigateToMap
import com.posttrip.journeydex.feature.reward.navigation.rewardScreen

@Composable
fun JourneydexNavHost(
    navController: NavHostController,
    modifier : Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = homeNavigationRoute
    ) {
        homeScreen(
            onNavigateMap = { contentId ->
                navController.navigateToMap(contentId)
            }
        )
        mapScreen()
        dexScreen()
        rewardScreen()
    }
}
